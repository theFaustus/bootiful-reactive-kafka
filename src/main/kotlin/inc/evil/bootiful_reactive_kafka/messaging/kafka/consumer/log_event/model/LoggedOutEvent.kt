package inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event.model

import inc.evil.bootiful_reactive_kafka.domain.DeviceType
import inc.evil.bootiful_reactive_kafka.domain.LogEventAudit
import inc.evil.bootiful_reactive_kafka.domain.LogEventType
import inc.evil.bootiful_reactive_kafka.domain.LogoutReason
import java.time.Instant

data class LoggedOutEvent(
    val userId: String,
    val timestamp: Instant = Instant.now(),
    val ipAddress: String,
    val deviceType: String,
    val sessionId: String? = null,
    val logoutReason: String,
    val browser: String? = null
) {
    fun toAuditEntity() = LogEventAudit(
        userId = userId,
        ipAddress = ipAddress,
        deviceType = runCatching { DeviceType.valueOf(deviceType) }.getOrElse { DeviceType.UNKNOWN },
        browser = browser,
        eventType = LogEventType.LOGOUT,
        loginMethod = null,
        logoutReason = runCatching { LogoutReason.valueOf(logoutReason) }.getOrElse { LogoutReason.UNKNOWN },
        sessionId = sessionId
    )
}
