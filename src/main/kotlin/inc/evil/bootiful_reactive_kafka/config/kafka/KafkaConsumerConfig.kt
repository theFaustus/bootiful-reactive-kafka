package inc.evil.bootiful_reactive_kafka.config.kafka

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import reactor.kafka.receiver.ReceiverOptions

@Configuration
class KafkaConsumerConfig {

    @Bean
    fun receiverOptions(props: KafkaProperties, @Value("\${spring.kafka.topic}") topic: String): ReceiverOptions<String, String> =
        ReceiverOptions.create<String, String>(props.buildConsumerProperties(null)).subscription(listOf(topic))

    @Bean
    fun consumerTemplate(options: ReceiverOptions<String, String>): ReactiveKafkaConsumerTemplate<String, String> = ReactiveKafkaConsumerTemplate(options)
}
