package inc.evil.bootiful_reactive_kafka.messaging.kafka.log_event

import inc.evil.bootiful_reactive_kafka.common.fixtures.LoggedInEventFixture
import inc.evil.bootiful_reactive_kafka.service.LogEventAuditService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord

class LoggedInEventConsumerTest {

    private val service: LogEventAuditService = mock()
    private val loggedOutEventConsumer: LoggedInEventConsumer = LoggedInEventConsumer(service)

    @Test
    fun handle_callsService() {
        val record = ReceiverRecord(ConsumerRecord("foo", 1, 1L, "user123", LoggedInEventFixture.of()), null)
        whenever(service.audit(record.value())).thenReturn(Mono.empty())

        loggedOutEventConsumer.handle(record).block()

        verify(service).audit(record.value())
    }
}