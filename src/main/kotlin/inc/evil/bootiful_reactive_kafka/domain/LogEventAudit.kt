package inc.evil.bootiful_reactive_kafka.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("log_event_audit")
data class LogEventAudit(
    @Id
    val id: Long? = null,

    @Column("user_id")
    val userId: String,

    @CreatedDate
    @Column("created_at")
    var createdAt: LocalDateTime? = null,

    @Column("ip_address")
    val ipAddress: String,

    @Column("device_type")
    val deviceType: DeviceType,

    @Column
    val browser: String?,

    @Column("event_type")
    val eventType: LogEventType,

    @Column("login_method")
    val loginMethod: LoginMethod?,

    @Column("logout_reason")
    val logoutReason: LogoutReason?,

    @Column("session_id")
    val sessionId: String?
)
