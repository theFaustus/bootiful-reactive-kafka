package inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.init_resources

import inc.evil.bootiful_reactive_kafka.common.AbstractTestcontainersTest
import inc.evil.bootiful_reactive_kafka.common.ComponentTest
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.init_resources.model.InitCommandType
import org.apache.kafka.clients.consumer.ConsumerConfig.*
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.system.OutputCaptureExtension
import java.time.Duration
import java.util.concurrent.TimeUnit


@ComponentTest
@ExtendWith(OutputCaptureExtension::class)
class InitResourcesMessageProducerTest : AbstractTestcontainersTest() {

    @Value("\${spring.kafka.producers.INIT_RESOURCES.topic}")
    private lateinit var topicName: String

    @Autowired
    private lateinit var initResourcesMessageProducer: InitResourcesMessageProducer

    @Test
    fun send_correctlyPublishesMessage() {
        val consumerProperties =
            mapOf(BOOTSTRAP_SERVERS_CONFIG to bootstrapServers(), GROUP_ID_CONFIG to "init-test-consumer", AUTO_OFFSET_RESET_CONFIG to "earliest")
        val consumer = KafkaConsumer(consumerProperties, StringDeserializer(), StringDeserializer())
        consumer.subscribe(listOf(topicName))
        val key = "userId"
        val value = InitCommandType.INIT.name

        initResourcesMessageProducer.send(key, value).block()

        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(20, TimeUnit.SECONDS)
            .untilAsserted {
                val records = consumer.poll(Duration.ofMillis(100))
                assertThat(records).isNotEmpty.hasSize(1)
                assertThat(records.first().key()).isEqualTo(key)
                assertThat(records.first().value()).isEqualTo(value)
            }
        consumer.unsubscribe()
    }
}
