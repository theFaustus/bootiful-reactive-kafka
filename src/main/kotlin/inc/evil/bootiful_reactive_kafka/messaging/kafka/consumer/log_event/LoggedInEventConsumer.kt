package inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event

import inc.evil.bootiful_reactive_kafka.config.kafka.consumer.AbstractReactiveKafkaConsumer
import inc.evil.bootiful_reactive_kafka.config.kafka.consumer.KafkaConsumerName.LOGGED_IN_EVENT
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event.model.LoggedInEvent
import inc.evil.bootiful_reactive_kafka.service.LogEventAuditService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord

@Component
class LoggedInEventConsumer(val logEventAuditService: LogEventAuditService) :
    AbstractReactiveKafkaConsumer<String, LoggedInEvent>(LOGGED_IN_EVENT) {

    override fun handle(record: ReceiverRecord<String, LoggedInEvent>): Mono<Void> =
        logEventAuditService.audit(record.value())

}

