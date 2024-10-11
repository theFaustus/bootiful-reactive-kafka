package inc.evil.bootiful_reactive_kafka.config.kafka.consumer

import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event.model.LoggedInEvent
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event.model.LoggedOutEvent
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.session_state.model.SessionHackAttemptEvent

enum class KafkaConsumerName(val eventType: String) {
    DEFAULT("Unknown"),
    SESSION_STATE_UPDATE(String::class.java.simpleName),
    LOGGED_IN_EVENT(LoggedInEvent::class.java.simpleName),
    LOGGED_OUT_EVENT(LoggedOutEvent::class.java.simpleName),
    SESSION_HACK_ATTEMPT(SessionHackAttemptEvent::class.java.simpleName)
}
