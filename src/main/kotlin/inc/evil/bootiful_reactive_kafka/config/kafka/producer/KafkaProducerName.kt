package inc.evil.bootiful_reactive_kafka.config.kafka.producer

import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.model.DestroyResourcesMessage

enum class KafkaProducerName(val eventType: String) {
    DEFAULT("Unknown"),
    INIT_RESOURCES(String::class.java.simpleName),
    DESTROY_RESOURCES(DestroyResourcesMessage::class.java.simpleName)
}
