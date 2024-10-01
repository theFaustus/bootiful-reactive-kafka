package inc.evil.bootiful_reactive_kafka.common.fixtures

import inc.evil.bootiful_reactive_kafka.domain.EventType
import inc.evil.bootiful_reactive_kafka.domain.LogEventAudit
import inc.evil.bootiful_reactive_kafka.domain.LoginMethod
import inc.evil.bootiful_reactive_kafka.domain.LogoutReason
import java.time.LocalDateTime

class LogEventAuditFixture {
    companion object {
        fun of(
            id: Long? = 1L,
            userId: String = "sponge_bob_squarepants",
            createdAt: LocalDateTime? = LocalDateTime.now(),
            ipAddress: String = "192.168.1.1",
            deviceType: String = "krabby_patty",
            browser: String? = "Nautilus Browser",
            eventType: EventType = EventType.LOGIN,
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