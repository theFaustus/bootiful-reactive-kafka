package inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.model

import inc.evil.bootiful_reactive_kafka.domain.LogoutReason
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event.model.LoggedOutEvent

data class DestroyResourcesMessage(val userId: String, val resources: List<ResourceType>) {

    companion object {
        fun from(loggedOutEvent: LoggedOutEvent): DestroyResourcesMessage =
            DestroyResourcesMessage(loggedOutEvent.userId, LogoutReason.valueOf(loggedOutEvent.logoutReason).resourcesToDestroy)
    }
}
