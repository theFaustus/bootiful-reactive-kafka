package inc.evil.bootiful_reactive_kafka.domain

import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.model.ResourceType

enum class LogoutReason(val resourcesToDestroy: List<ResourceType>) {
    USER_INITIATED(listOf(ResourceType.CONTAINER)),
    SESSION_TIMEOUT(listOf(ResourceType.CONTAINER)),
    ADMIN_ACTION(ResourceType.entries),
    SECURITY_POLICY(listOf(ResourceType.SECRET, ResourceType.SERVICE_ACCOUNT)),
    SYSTEM_LOGOUT(listOf(ResourceType.CONTAINER, ResourceType.DATABASE, ResourceType.STORAGE_VOLUME)),
    UNKNOWN(emptyList())
}

