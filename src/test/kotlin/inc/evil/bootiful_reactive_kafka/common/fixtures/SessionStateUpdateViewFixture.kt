package inc.evil.bootiful_reactive_kafka.common.fixtures

import inc.evil.bootiful_reactive_kafka.domain.SessionState
import inc.evil.bootiful_reactive_kafka.web.dto.SessionStateUpdateView
import java.time.LocalDateTime

class SessionStateUpdateViewFixture {

    companion object {
        fun of(
            id: Long? = 123L,
            userId: String = "SpongeBobSquarePants",
            createdAt: LocalDateTime? = LocalDateTime.now(),
            sessionState: SessionState = SessionState.INACTIVE
        ) = SessionStateUpdateView(
            id = id,
            userId = userId,
            createdAt = createdAt,
            sessionState = sessionState
        )
    }
}
