package inc.evil.bootiful_reactive_kafka.config.kafka

import inc.evil.bootiful_reactive_kafka.config.kafka.consumer.KafkaConsumerName
import inc.evil.bootiful_reactive_kafka.config.kafka.producer.KafkaProducerName
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.*


@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
data class KafkaConfigurationProperties(
    var consumers: Map<KafkaConsumerName, ConsumerProperties> = EnumMap(KafkaConsumerName::class.java),
    var producers: Map<KafkaProducerName, ProducerProperties> = EnumMap(KafkaProducerName::class.java)
) {

    data class ConsumerProperties(
        var topic: String? = null,
        var dltEnabled: Boolean = false,
        var properties: Map<String, String> = HashMap()
    )

    data class ProducerProperties(
        var topic: String? = null,
        var properties: Map<String, String> = HashMap()
    )
}


