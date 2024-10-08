package inc.evil.bootiful_reactive_kafka.config.kafka

import inc.evil.bootiful_reactive_kafka.messaging.kafka.log_event.model.LoggedInEvent
import inc.evil.bootiful_reactive_kafka.messaging.kafka.log_event.model.LoggedOutEvent

enum class KafkaConsumerName(val eventType: String) {
    DEFAULT("Unknown"),
    SESSION_STATE_UPDATE(String::class.java.simpleName),
    LOGGED_IN_EVENT(LoggedInEvent::class.java.simpleName),
    LOGGED_OUT_EVENT(LoggedOutEvent::class.java.simpleName)
}
