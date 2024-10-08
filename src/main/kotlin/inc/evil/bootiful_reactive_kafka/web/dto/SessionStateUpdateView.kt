package inc.evil.bootiful_reactive_kafka.web.dto

import inc.evil.bootiful_reactive_kafka.domain.SessionState
import inc.evil.bootiful_reactive_kafka.domain.SessionStateUpdateEventAudit
import java.time.LocalDateTime

data class SessionStateUpdateView(
    val id: Long?,
    val userId: String,
    val createdAt: LocalDateTime?,
    val sessionState: SessionState
) {
    companion object {
        fun from(entity: SessionStateUpdateEventAudit): SessionStateUpdateView =
            SessionStateUpdateView(
                id = entity.id,
                userId = entity.userId,
                createdAt = entity.createdAt,
                sessionState = entity.sessionState
            )
    }
}
