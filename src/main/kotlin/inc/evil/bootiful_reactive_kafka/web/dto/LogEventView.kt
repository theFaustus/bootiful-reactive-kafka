package inc.evil.bootiful_reactive_kafka.web.dto

import inc.evil.bootiful_reactive_kafka.domain.DeviceType
import inc.evil.bootiful_reactive_kafka.domain.LogEventAudit
import inc.evil.bootiful_reactive_kafka.domain.LoginMethod
import java.time.LocalDateTime

data class LogEventView(
    val id: Long?,
    val userId: String,
    val createdAt: LocalDateTime?,
    val ipAddress: String,
    val deviceType: DeviceType,
    val browser: String?,
    val eventType: String,
    val loginMethod: LoginMethod?
) {
    companion object {
        fun from(entity: LogEventAudit): LogEventView {
            return LogEventView(
                id = entity.id,
                userId = entity.userId,
                createdAt = entity.createdAt,
                ipAddress = entity.ipAddress,
                deviceType = entity.deviceType,
                browser = entity.browser,
                eventType = entity.eventType.name,
                loginMethod = entity.loginMethod
            )
        }
    }
}
