package inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources

import inc.evil.bootiful_reactive_kafka.common.AbstractTestcontainersTest
import inc.evil.bootiful_reactive_kafka.common.ComponentTest
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.model.DestroyResourcesMessage
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.model.ResourceType
import org.apache.kafka.clients.consumer.ConsumerConfig.*
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

@ComponentTest
class DestroyResourcesMessageProducerTest : AbstractTestcontainersTest() {

    @Autowired
    private lateinit var destroyResourcesMessageProducer: DestroyResourcesMessageProducer

    companion object {
        private val topicName = "test-topic-${UUID.randomUUID()}"

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.producers.DESTROY_RESOURCES.topic") { topicName }
        }
    }

    @Test
    fun send_correctlyPublishesMessage() {
        val consumerProperties =
            mapOf(BOOTSTRAP_SERVERS_CONFIG to bootstrapServers(), GROUP_ID_CONFIG to "destroy-test-consumer", AUTO_OFFSET_RESET_CONFIG to "earliest")
        val consumer = KafkaConsumer(consumerProperties, StringDeserializer(), StringDeserializer())
        consumer.subscribe(listOf(topicName))
        val key = "userId"
        val value = DestroyResourcesMessage(key, listOf(ResourceType.CONTAINER, ResourceType.DATABASE))
        //language=json
        val jsonPayload = """{"userId":"userId","resources":["CONTAINER","DATABASE"]}"""

        destroyResourcesMessageProducer.send(key, value).block()

        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(20, TimeUnit.SECONDS)
            .untilAsserted {
                val records = consumer.poll(Duration.ofMillis(100))
                assertThat(records).isNotEmpty.hasSize(1)
                assertThat(records.first().key()).isEqualTo(key)
                assertThat(records.first().value()).isEqualTo(jsonPayload)
            }
        consumer.unsubscribe()
    }
}
