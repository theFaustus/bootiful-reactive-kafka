package inc.evil.bootiful_reactive_kafka.repo

import inc.evil.bootiful_reactive_kafka.domain.SessionStateUpdateEventAudit
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface SessionStateUpdateEventAuditRepository : R2dbcRepository<SessionStateUpdateEventAudit, Long> {
    
    fun findByUserId(userId: String): Flux<SessionStateUpdateEventAudit>

}
