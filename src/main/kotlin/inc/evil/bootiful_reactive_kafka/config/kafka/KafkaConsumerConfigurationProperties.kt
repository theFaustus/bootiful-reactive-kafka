package inc.evil.bootiful_reactive_kafka.config.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.*


@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
data class KafkaConsumerConfigurationProperties(var consumers: Map<KafkaConsumerName, ConsumerProperties> = EnumMap(KafkaConsumerName::class.java)) {

    data class ConsumerProperties(
        var topic: String? = null,
        var properties: Map<String, String> = HashMap()
    )

}

