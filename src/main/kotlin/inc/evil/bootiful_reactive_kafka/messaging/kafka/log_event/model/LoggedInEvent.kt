package inc.evil.bootiful_reactive_kafka.messaging.kafka.log_event.model

import inc.evil.bootiful_reactive_kafka.domain.DeviceType
import inc.evil.bootiful_reactive_kafka.domain.EventType
import inc.evil.bootiful_reactive_kafka.domain.LogEventAudit
import inc.evil.bootiful_reactive_kafka.domain.LoginMethod
import java.time.Instant

data class LoggedInEvent(
    val userId: String,
    val timestamp: Instant = Instant.now(),
    val ipAddress: String,
    val deviceType: String,
    val browser: String? = null,
    val loginMethod: String,
    val sessionId: String? = null
) {
    fun toAuditEntity() = LogEventAudit(
        userId = userId,
        ipAddress = ipAddress,
        deviceType = runCatching { DeviceType.valueOf(deviceType) }.getOrElse { DeviceType.UNKNOWN },
        browser = browser,
        eventType = EventType.LOGIN,
        loginMethod = runCatching { LoginMethod.valueOf(loginMethod) }.getOrElse { LoginMethod.UNKNOWN },
        logoutReason = null,
        sessionId = sessionId
    )
}
