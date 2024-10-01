package inc.evil.bootiful_reactive_kafka.web.dto

import inc.evil.bootiful_reactive_kafka.common.fixtures.LogEventAuditFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LogEventViewTest {
    @Test
    fun from_withValidLogEventAudit_returnsCorrectLogEventView() {
        val logEventAudit = LogEventAuditFixture.of()

        val result = LogEventView.from(logEventAudit)

        assertThat(result.id).isEqualTo(logEventAudit.id)
        assertThat(result.userId).isEqualTo(logEventAudit.userId)
        assertThat(result.createdAt).isEqualTo(logEventAudit.createdAt)
        assertThat(result.ipAddress).isEqualTo(logEventAudit.ipAddress)
        assertThat(result.deviceType).isEqualTo(logEventAudit.deviceType)
        assertThat(result.browser).isEqualTo(logEventAudit.browser)
        assertThat(result.eventType).isEqualTo(logEventAudit.eventType.name)
        assertThat(result.loginMethod).isEqualTo(logEventAudit.loginMethod)
    }
}
