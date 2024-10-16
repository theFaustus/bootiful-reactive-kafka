package inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources

import inc.evil.bootiful_reactive_kafka.config.kafka.producer.AbstractReactiveKafkaProducer
import inc.evil.bootiful_reactive_kafka.config.kafka.producer.KafkaProducerName.DESTROY_RESOURCES
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.model.DestroyResourcesMessage
import org.springframework.stereotype.Component

@Component
class DestroyResourcesMessageProducer : AbstractReactiveKafkaProducer<String, DestroyResourcesMessage>(DESTROY_RESOURCES)
