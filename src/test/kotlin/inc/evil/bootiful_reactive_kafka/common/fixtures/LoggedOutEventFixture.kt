package inc.evil.bootiful_reactive_kafka.common.fixtures

import inc.evil.bootiful_reactive_kafka.messaging.kafka.log_event.model.LoggedOutEvent
import java.time.Instant

class LoggedOutEventFixture {
    companion object {
        fun of(
            userId: String = "SpongeBobSquarePants",
            timestamp: Instant = Instant.parse("2024-10-08T17:00:00.456Z"),
            ipAddress: String = "1.1.1.3",
            deviceType: String = "DESKTOP",
            sessionId: String? = "sessionId123",
            logoutReason: String = "USER_INITIATED",
            browser: String? = "Chrome"
        ) = LoggedOutEvent(
            userId = userId,
            timestamp = timestamp,
            ipAddress = ipAddress,
            deviceType = deviceType,
            sessionId = sessionId,
            logoutReason = logoutReason,
            browser = browser
        )
    }
}
