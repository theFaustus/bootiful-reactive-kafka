package inc.evil.bootiful_reactive_kafka.service

import inc.evil.bootiful_reactive_kafka.config.tracing.observe
import inc.evil.bootiful_reactive_kafka.repo.LogEventAuditRepository
import inc.evil.bootiful_reactive_kafka.web.dto.LogEventView
import io.micrometer.observation.ObservationRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class LogEventAuditService(private val repository: LogEventAuditRepository, private val registry: ObservationRegistry) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun findUserEvents(userId: String): Flux<LogEventView> =
        repository.findByUserId(userId)
            .map { LogEventView.from(it) }
            .observe(registry, "findUserEvents") { mapOf("userId" to userId) }
            .doOnNext { log.debug("Retrieved following LogEventAudit ${it.id} - ${it.userId}") }
}
