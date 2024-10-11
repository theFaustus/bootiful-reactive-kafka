package inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event.model

import inc.evil.bootiful_reactive_kafka.common.fixtures.LoggedOutEventFixture
import inc.evil.bootiful_reactive_kafka.domain.DeviceType
import inc.evil.bootiful_reactive_kafka.domain.LogEventType
import inc.evil.bootiful_reactive_kafka.domain.LogoutReason
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LoggedOutEventTest {

    @Test
    fun from_withValidLoggedOutEvent_returnsCorrectLogEventAudit() {
        val loggedOutEvent = LoggedOutEventFixture.of()

        val result = loggedOutEvent.toAuditEntity()

        assertThat(result.userId).isEqualTo(loggedOutEvent.userId)
        assertThat(result.ipAddress).isEqualTo(loggedOutEvent.ipAddress)
        assertThat(result.deviceType).isEqualTo(DeviceType.DESKTOP)
        assertThat(result.browser).isEqualTo(loggedOutEvent.browser)
        assertThat(result.eventType).isEqualTo(LogEventType.LOGOUT)
        assertThat(result.loginMethod).isNull()
        assertThat(result.logoutReason).isEqualTo(runCatching { LogoutReason.valueOf(loggedOutEvent.logoutReason) }.getOrElse { LogoutReason.UNKNOWN })
        assertThat(result.sessionId).isEqualTo(loggedOutEvent.sessionId)
    }

    @Test
    fun from_withUnknownDeviceType_returnsUnknownDeviceType() {
        val loggedOutEvent = LoggedOutEventFixture.of(deviceType = "UNKNOWN_TYPE")

        val result = loggedOutEvent.toAuditEntity()

        assertThat(result.deviceType).isEqualTo(DeviceType.UNKNOWN)
    }

    @Test
    fun from_withInvalidLogoutReason_returnsUnknownLogoutReason() {
        val loggedOutEvent = LoggedOutEventFixture.of(logoutReason = "INVALID_REASON")

        val result = loggedOutEvent.toAuditEntity()

        assertThat(result.logoutReason).isEqualTo(LogoutReason.UNKNOWN)
    }

}
