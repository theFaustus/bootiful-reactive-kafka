package inc.evil.bootiful_reactive_kafka.service

import inc.evil.bootiful_reactive_kafka.common.fixtures.LogEventAuditFixture
import inc.evil.bootiful_reactive_kafka.common.fixtures.LoggedInEventFixture
import inc.evil.bootiful_reactive_kafka.common.fixtures.LoggedOutEventFixture
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.DestroyResourcesMessageProducer
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.model.DestroyResourcesMessage
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.model.ResourceType
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.init_resources.InitResourcesMessageProducer
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.init_resources.model.InitCommandType
import inc.evil.bootiful_reactive_kafka.repo.LogEventAuditRepository
import io.micrometer.observation.ObservationRegistry
import org.apache.kafka.common.header.internals.RecordHeader
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class LogEventServiceTest {

    private val repository: LogEventAuditRepository = mock()
    private val initResourcesMessageProducer: InitResourcesMessageProducer = mock()
    private val destroyResourcesMessageProducer: DestroyResourcesMessageProducer = mock()
    private val registry: ObservationRegistry = ObservationRegistry.NOOP
    private val service: LogEventService = LogEventService(repository, initResourcesMessageProducer, destroyResourcesMessageProducer, registry)

    @Test
    fun findByUserId_withValidByUserId_returnsMappedLogEventViews() {
        val userId = "testUserId"
        val logEvent = LogEventAuditFixture.of()
        whenever(repository.findByUserId(userId)).thenReturn(Flux.just(logEvent))

        val result = service.findByUserId(userId).collectList().block()

        assertThat(result).hasSize(1)
        assertThat(result?.first()?.id).isEqualTo(logEvent.id)
        assertThat(result?.first()?.userId).isEqualTo(logEvent.userId)
        verify(repository).findByUserId(userId)
    }

    @Test
    fun findByUserId_withEmptyResult_returnsEmptyList() {
        val userId = "testUserId"
        whenever(repository.findByUserId(userId)).thenReturn(Flux.empty())

        val result = service.findByUserId(userId).collectList().block()

        assertThat(result).isEmpty()
        verify(repository).findByUserId(userId)
    }

    @Test
    fun handle_withLoggedInEvent_savesAuditEntity() {
        val loggedInEvent = LoggedInEventFixture.of()
        val logEventAudit = loggedInEvent.toAuditEntity()
        whenever(repository.save(logEventAudit)).thenReturn(Mono.just(logEventAudit))
        whenever(initResourcesMessageProducer.send(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(Mono.empty())

        service.handle(loggedInEvent).block()

        verify(repository).save(logEventAudit)
        verify(initResourcesMessageProducer)
            .send(loggedInEvent.userId, InitCommandType.INIT.name, setOf(RecordHeader("ipAddress", loggedInEvent.ipAddress.toByteArray())))
    }

    @Test
    fun handle_withLoggedOutEvent_savesAuditEntity() {
        val loggedOutEvent = LoggedOutEventFixture.of()
        val logEventAudit = loggedOutEvent.toAuditEntity()
        whenever(repository.save(logEventAudit)).thenReturn(Mono.just(logEventAudit))
        whenever(destroyResourcesMessageProducer.send(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(Mono.empty())

        service.handle(loggedOutEvent).block()

        verify(repository).save(logEventAudit)
        verify(destroyResourcesMessageProducer)
            .send(loggedOutEvent.userId, DestroyResourcesMessage(loggedOutEvent.userId, listOf(ResourceType.CONTAINER)))
    }
}
