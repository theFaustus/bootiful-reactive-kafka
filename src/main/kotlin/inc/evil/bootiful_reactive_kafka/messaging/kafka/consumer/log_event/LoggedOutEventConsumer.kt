package inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event

import inc.evil.bootiful_reactive_kafka.config.kafka.consumer.AbstractReactiveKafkaConsumer
import inc.evil.bootiful_reactive_kafka.config.kafka.consumer.KafkaConsumerName.LOGGED_OUT_EVENT
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event.model.LoggedOutEvent
import inc.evil.bootiful_reactive_kafka.service.LogEventAuditService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord

@Component
class LoggedOutEventConsumer(val logEventAuditService: LogEventAuditService) :
    AbstractReactiveKafkaConsumer<String, LoggedOutEvent>(LOGGED_OUT_EVENT) {

    override fun handle(record: ReceiverRecord<String, LoggedOutEvent>): Mono<Void> =
        logEventAuditService.audit(record.value())

}

