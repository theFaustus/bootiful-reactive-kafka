package inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.session_state

import inc.evil.bootiful_reactive_kafka.service.SessionStateUpdateEventAuditService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord

class SessionStateUpdateEventConsumerTest {

    private val service: SessionStateUpdateEventAuditService = mock()
    private val sessionStateUpdateEventConsumer: SessionStateUpdateEventConsumer = SessionStateUpdateEventConsumer(service)

    @Test
    fun handle_callsService() {
        val record = ReceiverRecord(ConsumerRecord("foo", 1, 1L, "user123", "ACTIVE"), null)
        whenever(service.audit(record.key(), record.value())).thenReturn(Mono.empty())

        sessionStateUpdateEventConsumer.handle(record).block()

        verify(service).audit(record.key(), record.value())
    }
}
