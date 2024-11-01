package inc.evil.bootiful_reactive_kafka.config.kafka.consumer

import inc.evil.bootiful_reactive_kafka.config.kafka.KafkaConfigurationProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.kafka.receiver.ReceiverOptions

@Component
class KafkaReceiverOptionsFactory(val config: KafkaConfigurationProperties) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun <K, V> createReceiverOptions(kafkaConsumerName: KafkaConsumerName): ReceiverOptions<K, V> {
        log.debug("Creating receiver options for Consumer=[{}]", kafkaConsumerName)
        val defaultProps = config.consumers[KafkaConsumerName.DEFAULT]
            ?: throw IllegalStateException("Default consumer configuration not found")
        val specificProps = config.consumers[kafkaConsumerName]
            ?: throw IllegalArgumentException("Consumer configuration not found for: $kafkaConsumerName")

        val consumerProperties =
            KafkaConfigurationProperties.ConsumerProperties(specificProps.topic, specificProps.dltEnabled, defaultProps.properties + specificProps.properties)

        log.debug("Computed consumer properties for {} : {}", kafkaConsumerName, consumerProperties)

        val options = ReceiverOptions.create<K, V>(consumerProperties.properties)
            .subscription(listOf(consumerProperties.topic ?: throw IllegalArgumentException("Missing <topic> field for: $kafkaConsumerName")))
            .addAssignListener { log.info("Consumer {} partitions assigned {}", kafkaConsumerName, it) }
            .addRevokeListener { log.info("Consumer {} partitions revoked {}", kafkaConsumerName, it) }

        return options
    }

    fun isDeadLetterTopicEnabled(kafkaConsumerName: KafkaConsumerName) = (config.consumers[kafkaConsumerName]?.dltEnabled) ?: false
}
