package inc.evil.bootiful_reactive_kafka.service

import inc.evil.bootiful_reactive_kafka.common.fixtures.SessionStateUpdateEventAuditFixture
import inc.evil.bootiful_reactive_kafka.domain.SessionState
import inc.evil.bootiful_reactive_kafka.domain.SessionStateUpdateEventAudit
import inc.evil.bootiful_reactive_kafka.repo.SessionStateUpdateEventAuditRepository
import io.micrometer.observation.ObservationRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class SessionStateUpdateEventAuditServiceTest {

    private val repository: SessionStateUpdateEventAuditRepository = mock()
    private val registry: ObservationRegistry = ObservationRegistry.NOOP
    private val service: SessionStateUpdateEventAuditService = SessionStateUpdateEventAuditService(repository, registry)

    @Test
    fun findByUserId_withValidUserId_returnsMappedSessionStateUpdateViews() {
        val userId = "testUserId"
        val sessionEventAudit = SessionStateUpdateEventAuditFixture.of()
        whenever(repository.findByUserId(userId)).thenReturn(Flux.just(sessionEventAudit))

        val result = service.findByUserId(userId).collectList().block()

        assertThat(result).hasSize(1)
        assertThat(result?.first()?.id).isEqualTo(sessionEventAudit.id)
        assertThat(result?.first()?.userId).isEqualTo(sessionEventAudit.userId)
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
    fun audit_withValidState_savesSessionStateUpdateAuditEntity() {
        val userId = "testUserId"
        val sessionState = "ACTIVE"
        val sessionEventAudit = SessionStateUpdateEventAudit(userId = userId, sessionState = SessionState.valueOf(sessionState))
        whenever(repository.save(sessionEventAudit)).thenReturn(Mono.just(sessionEventAudit))

        service.audit(userId, sessionState).block()

        verify(repository).save(sessionEventAudit)
    }

}
