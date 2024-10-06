package inc.evil.bootiful_reactive_kafka.messaging.kafka.session_state

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class SessionStateUpdateEventConsumer(val consumerTemplate: ReactiveKafkaConsumerTemplate<String, String>) : CommandLineRunner {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun run(vararg args: String?) {
        consumerTemplate
            .receive()
            .concatMap { receiverRecord ->
                Mono.just(receiverRecord)
                    .doOnError { log.error("Encountered [{}] during process of SessionStateUpdate", it.message, it) }
                    .doOnNext { r -> log.debug("Received {} with value={}", r.key(), r.value()) }
                    .doFinally { receiverRecord.receiverOffset().acknowledge() }
            }.subscribe()
    }

}

