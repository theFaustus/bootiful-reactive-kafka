package inc.evil.bootiful_reactive_kafka.service

import inc.evil.bootiful_reactive_kafka.domain.SessionState
import inc.evil.bootiful_reactive_kafka.domain.SessionStateUpdateEventAudit
import inc.evil.bootiful_reactive_kafka.repo.SessionStateUpdateEventAuditRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SessionStateUpdateEventAuditService(private val repository: SessionStateUpdateEventAuditRepository) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun save(userId: String, state: String): Mono<Void> =
        repository.save(SessionStateUpdateEventAudit(userId = userId, sessionState = SessionState.valueOf(state)))
            .doOnNext { log.debug("Saved {}", it) }.then()
}
