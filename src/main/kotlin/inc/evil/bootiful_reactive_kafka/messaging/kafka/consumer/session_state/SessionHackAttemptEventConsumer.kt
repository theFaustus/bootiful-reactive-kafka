package inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.session_state

import inc.evil.bootiful_reactive_kafka.config.kafka.consumer.AbstractReactiveKafkaConsumer
import inc.evil.bootiful_reactive_kafka.config.kafka.consumer.KafkaConsumerName.SESSION_HACK_ATTEMPT
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.session_state.model.SessionHackAttemptEvent
import inc.evil.bootiful_reactive_kafka.service.SessionStateUpdateEventAuditService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord

@Component
class SessionHackAttemptEventConsumer(val sessionStateUpdateEventAuditService: SessionStateUpdateEventAuditService) :
    AbstractReactiveKafkaConsumer<String, SessionHackAttemptEvent>(SESSION_HACK_ATTEMPT) {

    override fun handle(record: ReceiverRecord<String, SessionHackAttemptEvent>): Mono<Void> =
        sessionStateUpdateEventAuditService.audit(record.key(), record.value())

}

