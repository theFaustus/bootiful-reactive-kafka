package inc.evil.bootiful_reactive_kafka.common.fixtures

import inc.evil.bootiful_reactive_kafka.domain.SessionState
import inc.evil.bootiful_reactive_kafka.domain.SessionStateUpdateEventAudit
import java.time.LocalDateTime

class SessionStateUpdateEventAuditFixture {

    companion object {
        fun of(
            id: Long? = 123L,
            userId: String = "SpongeBobSquarePants",
            createdAt: LocalDateTime? = LocalDateTime.now(),
            sessionState: SessionState = SessionState.ACTIVE
        ) = SessionStateUpdateEventAudit(
            id = id,
            userId = userId,
            createdAt = createdAt,
            sessionState = sessionState
        )
    }
}
