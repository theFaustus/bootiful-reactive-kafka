package inc.evil.bootiful_reactive_kafka.service

import inc.evil.bootiful_reactive_kafka.config.tracing.observe
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event.model.LoggedInEvent
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event.model.LoggedOutEvent
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.DestroyResourcesMessageProducer
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.model.DestroyResourcesMessage
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.init_resources.InitResourcesMessageProducer
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.init_resources.model.InitCommandType.INIT
import inc.evil.bootiful_reactive_kafka.repo.LogEventAuditRepository
import inc.evil.bootiful_reactive_kafka.web.dto.LogEventView
import io.micrometer.observation.ObservationRegistry
import org.apache.kafka.common.header.internals.RecordHeader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class LogEventService(
    private val repository: LogEventAuditRepository,
    private val initResourcesMessageProducer: InitResourcesMessageProducer,
    private val destroyResourcesMessageProducer: DestroyResourcesMessageProducer,
    private val registry: ObservationRegistry
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun handle(event: LoggedInEvent): Mono<Void> =
        repository.save(event.toAuditEntity())
            .doOnNext { log.debug("Audited {}", it) }
            .doOnSuccess {
                initResourcesMessageProducer.send(event.userId, INIT.name, mutableSetOf(RecordHeader("ipAddress", event.ipAddress.toByteArray())))
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe()
            }
            .then()

    fun handle(event: LoggedOutEvent): Mono<Void> =
        repository.save(event.toAuditEntity())
            .doOnNext { log.debug("Audited {}", it) }
            .doOnSuccess {
                destroyResourcesMessageProducer.send(event.userId, DestroyResourcesMessage.from(event))
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe()
            }
            .then()

    fun findByUserId(userId: String): Flux<LogEventView> =
        repository.findByUserId(userId)
            .map { LogEventView.from(it) }
            .observe(registry, "findUserEvents") { mapOf("userId" to userId) }
            .doOnNext { log.debug("Retrieved following LogEventAudit ${it.id} - ${it.userId}") }
}
