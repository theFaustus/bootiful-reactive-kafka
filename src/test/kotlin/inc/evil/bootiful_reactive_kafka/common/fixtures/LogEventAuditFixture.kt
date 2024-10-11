package inc.evil.bootiful_reactive_kafka.common.fixtures

import inc.evil.bootiful_reactive_kafka.domain.*
import java.time.LocalDateTime

class LogEventAuditFixture {
    companion object {
        fun of(
            id: Long? = 1L,
            userId: String = "SpongeBobSquarePants",
            createdAt: LocalDateTime? = LocalDateTime.now(),
            ipAddress: String = "1.1.1.1",
            deviceType: DeviceType = DeviceType.DESKTOP,
            browser: String? = "Nautilus Browser",
            eventType: LogEventType = LogEventType.LOGIN,
            loginMethod: LoginMethod? = LoginMethod.PASSWORD,
            logoutReason: LogoutReason? = null,
            sessionId: String? = "session_42"
        ) = LogEventAudit(
            id = id,
            userId = userId,
            createdAt = createdAt,
            ipAddress = ipAddress,
            deviceType = deviceType,
            browser = browser,
            eventType = eventType,
            loginMethod = loginMethod,
            logoutReason = logoutReason,
            sessionId = sessionId
        )
    }
}
