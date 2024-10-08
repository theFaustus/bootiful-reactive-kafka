package inc.evil.bootiful_reactive_kafka.messaging.kafka.log_event.model

import inc.evil.bootiful_reactive_kafka.common.fixtures.LoggedInEventFixture
import inc.evil.bootiful_reactive_kafka.domain.DeviceType
import inc.evil.bootiful_reactive_kafka.domain.EventType
import inc.evil.bootiful_reactive_kafka.domain.LoginMethod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LoggedInEventTest {

    @Test
    fun toAuditEntity_withValidLoggedInEvent_returnsCorrectLogEventAudit() {
        val loggedInEvent = LoggedInEventFixture.of()

        val result = loggedInEvent.toAuditEntity()

        assertThat(result.userId).isEqualTo(loggedInEvent.userId)
        assertThat(result.ipAddress).isEqualTo(loggedInEvent.ipAddress)
        assertThat(result.deviceType).isEqualTo(DeviceType.DESKTOP)
        assertThat(result.browser).isEqualTo(loggedInEvent.browser)
        assertThat(result.eventType).isEqualTo(EventType.LOGIN)
        assertThat(result.loginMethod).isEqualTo(LoginMethod.PASSWORD)
        assertThat(result.logoutReason).isNull()
        assertThat(result.sessionId).isEqualTo(loggedInEvent.sessionId)
    }

    @Test
    fun toAuditEntity_withInvalidDeviceType_returnsUnknownDeviceType() {
        val loggedInEvent = LoggedInEventFixture.of(deviceType = "INVALID_DEVICE")

        val result = loggedInEvent.toAuditEntity()

        assertThat(result.deviceType).isEqualTo(DeviceType.UNKNOWN)
    }

    @Test
    fun toAuditEntity_withInvalidLoginMethod_returnsUnknownLoginMethod() {
        val loggedInEvent = LoggedInEventFixture.of(loginMethod = "INVALID_METHOD")

        val result = loggedInEvent.toAuditEntity()

        assertThat(result.loginMethod).isEqualTo(LoginMethod.UNKNOWN)
    }
}
