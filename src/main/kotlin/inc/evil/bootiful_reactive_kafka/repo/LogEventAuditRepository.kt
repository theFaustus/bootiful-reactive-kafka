package inc.evil.bootiful_reactive_kafka.repo

import inc.evil.bootiful_reactive_kafka.domain.LogEventAudit
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface LogEventAuditRepository : R2dbcRepository<LogEventAudit, Long> {
    
    fun findByUserId(userId: String): Flux<LogEventAudit>

}
