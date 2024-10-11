package inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.session_state

import inc.evil.bootiful_reactive_kafka.config.kafka.consumer.AbstractReactiveKafkaConsumer
import inc.evil.bootiful_reactive_kafka.config.kafka.consumer.KafkaConsumerName.SESSION_STATE_UPDATE
import inc.evil.bootiful_reactive_kafka.service.SessionStateUpdateEventAuditService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord

@Component
class SessionStateUpdateEventConsumer(val sessionStateUpdateEventAuditService: SessionStateUpdateEventAuditService) :
    AbstractReactiveKafkaConsumer<String, String>(SESSION_STATE_UPDATE) {

    override fun handle(record: ReceiverRecord<String, String>): Mono<Void> =
        sessionStateUpdateEventAuditService.audit(record.key(), record.value())

}

