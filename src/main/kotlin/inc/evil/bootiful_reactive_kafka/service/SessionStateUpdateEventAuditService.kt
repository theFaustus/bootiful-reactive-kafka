package inc.evil.bootiful_reactive_kafka.service

import inc.evil.bootiful_reactive_kafka.config.tracing.observe
import inc.evil.bootiful_reactive_kafka.domain.SessionState
import inc.evil.bootiful_reactive_kafka.domain.SessionStateUpdateEventAudit
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.session_state.model.SessionHackAttemptEvent
import inc.evil.bootiful_reactive_kafka.repo.SessionStateUpdateEventAuditRepository
import inc.evil.bootiful_reactive_kafka.web.dto.SessionStateUpdateView
import io.micrometer.observation.ObservationRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class SessionStateUpdateEventAuditService(private val repository: SessionStateUpdateEventAuditRepository, private val registry: ObservationRegistry) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun findByUserId(userId: String): Flux<SessionStateUpdateView> =
        repository.findByUserId(userId)
            .map { SessionStateUpdateView.from(it) }
            .observe(registry, "findUserEvents") { mapOf("userId" to userId) }
            .doOnNext { log.debug("Retrieved following LogEventAudit ${it.id} - ${it.userId}") }

    fun audit(userId: String, state: String): Mono<Void> =
        repository.save(SessionStateUpdateEventAudit(userId = userId, sessionState = SessionState.valueOf(state)))
            .doOnNext { log.debug("Audited {}", it) }.then()

    fun audit(userId: String, hackAttemptEvent: SessionHackAttemptEvent): Mono<Void> =
        repository.save(SessionStateUpdateEventAudit(userId = userId, sessionState = SessionState.HACK_ATTEMPT))
            .doOnNext { log.debug("Audited {}", it) }.then()
}
