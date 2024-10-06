package inc.evil.bootiful_reactive_kafka.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("session_state_update_event_audit")
data class SessionStateUpdateEventAudit(
    @Id
    val id: Long? = null,

    @Column("user_id")
    val userId: String,

    @CreatedDate
    @Column("created_at")
    var createdAt: LocalDateTime? = null,

    @Column("session_state")
    val sessionState: SessionState
)
