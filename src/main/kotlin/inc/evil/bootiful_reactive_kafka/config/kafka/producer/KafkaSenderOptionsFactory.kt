package inc.evil.bootiful_reactive_kafka.config.kafka.producer

import inc.evil.bootiful_reactive_kafka.config.kafka.KafkaConfigurationProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.kafka.sender.SenderOptions

@Component
class KafkaSenderOptionsFactory(val config: KafkaConfigurationProperties) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun <K, V> createSenderOptions(kafkaProducerName: KafkaProducerName): SenderOptions<K, V> {
        log.debug("Creating receiver options for Producer=[{}]", kafkaProducerName)
        val defaultProps = config.producers[KafkaProducerName.DEFAULT]
            ?: throw IllegalStateException("Default producer configuration not found")
        val specificProps = config.producers[kafkaProducerName]
            ?: throw IllegalArgumentException("Producer configuration not found for: $kafkaProducerName")

        val producerProperties = KafkaConfigurationProperties.ProducerProperties(specificProps.topic, defaultProps.properties + specificProps.properties)

        log.debug("Computed producer properties for {} : {}", kafkaProducerName, producerProperties)
        return SenderOptions.create(producerProperties.properties)
    }

    fun getSenderTopic(kafkaProducerName: KafkaProducerName) =
        (config.producers[kafkaProducerName]?.topic ?: throw IllegalArgumentException("Producer topic not found for: $kafkaProducerName"))
}
