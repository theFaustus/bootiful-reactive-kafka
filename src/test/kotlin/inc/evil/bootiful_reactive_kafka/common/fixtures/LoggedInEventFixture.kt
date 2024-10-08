package inc.evil.bootiful_reactive_kafka.common.fixtures

import inc.evil.bootiful_reactive_kafka.messaging.kafka.log_event.model.LoggedInEvent
import java.time.Instant

class LoggedInEventFixture {
    companion object {
        fun of(
            userId: String = "SpongeBobSquarePants",
            timestamp: Instant = Instant.parse("2024-10-08T12:45:00.123Z"),
            ipAddress: String = "1.1.1.2",
            deviceType: String = "DESKTOP",
            browser: String? = "Firefox",
            loginMethod: String = "PASSWORD",
            sessionId: String? = "sessionId456"
        ) = LoggedInEvent(
            userId = userId,
            timestamp = timestamp,
            ipAddress = ipAddress,
            deviceType = deviceType,
            browser = browser,
            loginMethod = loginMethod,
            sessionId = sessionId
        )
    }
}
