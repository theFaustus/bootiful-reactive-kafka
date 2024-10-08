package inc.evil.bootiful_reactive_kafka.messaging.kafka.session_state

import inc.evil.bootiful_reactive_kafka.config.kafka.AbstractReactiveKafkaConsumer
import inc.evil.bootiful_reactive_kafka.config.kafka.KafkaConsumerName.SESSION_STATE_UPDATE
import inc.evil.bootiful_reactive_kafka.service.SessionStateUpdateEventAuditService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord

@Component
class SessionStateUpdateEventConsumer(val sessionStateUpdateEventAuditService: SessionStateUpdateEventAuditService) :
    AbstractReactiveKafkaConsumer<String, String>(SESSION_STATE_UPDATE) {

    override fun handle(record: ReceiverRecord<String, String>): Mono<Void> =
        sessionStateUpdateEventAuditService.save(record.key(), record.value())

}

