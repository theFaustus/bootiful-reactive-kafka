package inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.init_resources

import inc.evil.bootiful_reactive_kafka.config.kafka.producer.AbstractReactiveKafkaProducer
import inc.evil.bootiful_reactive_kafka.config.kafka.producer.KafkaProducerName.INIT_RESOURCES
import org.springframework.stereotype.Component

@Component
class InitResourcesMessageProducer : AbstractReactiveKafkaProducer<String, String>(INIT_RESOURCES)
