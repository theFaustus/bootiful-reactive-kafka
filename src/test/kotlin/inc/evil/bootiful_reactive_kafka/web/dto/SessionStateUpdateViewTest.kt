package inc.evil.bootiful_reactive_kafka.web.dto

import inc.evil.bootiful_reactive_kafka.common.fixtures.SessionStateUpdateEventAuditFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SessionStateUpdateViewTest {

    @Test
    fun from_withValidSessionStateUpdateEventAudit_returnsCorrectSessionStateUpdateView() {
        val sessionStateUpdateEventAudit = SessionStateUpdateEventAuditFixture.of()

        val result = SessionStateUpdateView.from(sessionStateUpdateEventAudit)

        assertThat(result.id).isEqualTo(sessionStateUpdateEventAudit.id)
        assertThat(result.userId).isEqualTo(sessionStateUpdateEventAudit.userId)
        assertThat(result.createdAt).isEqualTo(sessionStateUpdateEventAudit.createdAt)
        assertThat(result.sessionState).isEqualTo(sessionStateUpdateEventAudit.sessionState)
    }
}
