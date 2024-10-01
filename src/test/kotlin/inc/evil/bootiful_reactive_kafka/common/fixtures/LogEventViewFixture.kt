package inc.evil.bootiful_reactive_kafka.common.fixtures

import inc.evil.bootiful_reactive_kafka.domain.LoginMethod
import inc.evil.bootiful_reactive_kafka.web.dto.LogEventView
import java.time.LocalDateTime

class LogEventViewFixture {
    companion object {
        fun of(
            id: Long? = 1L,
            userId: String = "sponge_bob_squarepants",
            createdAt: LocalDateTime? = LocalDateTime.now(),
            ipAddress: String = "192.168.1.1",
            deviceType: String = "krabby_patty",
            browser: String? = "Nautilus Browser",
            eventType: String = "LOGIN",
            loginMethod: LoginMethod? = LoginMethod.SSO
        ) = LogEventView(
            id = id,
            userId = userId,
            createdAt = createdAt,
            ipAddress = ipAddress,
            deviceType = deviceType,
            browser = browser,
            eventType = eventType,
            loginMethod = loginMethod
        )
    }
}
