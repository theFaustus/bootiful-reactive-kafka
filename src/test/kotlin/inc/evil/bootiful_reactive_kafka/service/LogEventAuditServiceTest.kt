package inc.evil.bootiful_reactive_kafka.service

import inc.evil.bootiful_reactive_kafka.common.fixtures.LogEventAuditFixture
import inc.evil.bootiful_reactive_kafka.repo.LogEventAuditRepository
import io.micrometer.observation.ObservationRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import reactor.core.publisher.Flux

class LogEventAuditServiceTest {

    private val repository: LogEventAuditRepository = mock()
    private val registry: ObservationRegistry = ObservationRegistry.NOOP
    private val service: LogEventAuditService = LogEventAuditService(repository, registry)

    @Test
    fun findUserEvents_withValidUserId_returnsMappedLogEventViews() {
        val userId = "testUserId"
        val logEvent = LogEventAuditFixture.of()
        whenever(repository.findByUserId(userId)).thenReturn(Flux.just(logEvent))

        val result = service.findUserEvents(userId).collectList().block()

        assertThat(result).hasSize(1)
        assertThat(result?.first()?.id).isEqualTo(logEvent.id)
        assertThat(result?.first()?.userId).isEqualTo(logEvent.userId)
        verify(repository).findByUserId(userId)
    }

    @Test
    fun findUserEvents_withEmptyResult_returnsEmptyList() {
        val userId = "testUserId"
        whenever(repository.findByUserId(userId)).thenReturn(Flux.empty())

        val result = service.findUserEvents(userId).collectList().block()

        assertThat(result).isEmpty()
        verify(repository).findByUserId(userId)
    }

}
