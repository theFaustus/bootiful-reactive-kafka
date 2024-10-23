package inc.evil.bootiful_reactive_kafka.config.kafka.consumer

import inc.evil.bootiful_reactive_kafka.config.kafka.producer.ReactiveKafkaDeadLetterTopicProducer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord
import reactor.util.retry.Retry
import java.time.Duration

abstract class AbstractReactiveKafkaConsumer<K : Any, V>(private val consumerName: KafkaConsumerName) : CommandLineRunner {

    companion object {
        private const val DEFAULT_RETRY_MAX_ATTEMPTS = 3L
    }

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var kafkaReceiverOptionsFactory: KafkaReceiverOptionsFactory

    @Autowired
    private lateinit var reactiveKafkaDeadLetterTopicProducer: ReactiveKafkaDeadLetterTopicProducer

    private val kafkaConsumerTemplate: ReactiveKafkaConsumerTemplate<K, V> by lazy {
        ReactiveKafkaConsumerTemplate(kafkaReceiverOptionsFactory.createReceiverOptions<K, V>(consumerName))
    }

    override fun run(vararg args: String?) {
        kafkaConsumerTemplate.receive()
            .doOnError { log.error("Encountered [{}] during process of ${consumerName.eventType}", it.message, it) }
            .concatMap { consume(it) }
            .subscribe()
    }

    protected open fun consume(record: ReceiverRecord<K, V>): Mono<Void> =
        Mono.just(record)
            .doOnNext { r -> log.debug("Received {} with key={} and value={}", consumerName.eventType, r.key(), r.value()) }
            .flatMap { handle(it) }
            .retryWhen(getRetrySpec(record))
            .onErrorResume { handleProcessingError(record, it) }
            .doFinally { record.receiverOffset().acknowledge() }
            .onErrorComplete()
            .then()

    protected open fun handleProcessingError(record: ReceiverRecord<K, V>, ex: Throwable): Mono<Void> =
        (Mono.error<Void>(ex)
            .takeUnless { kafkaReceiverOptionsFactory.isDeadLetterTopicEnabled(consumerName) }
            ?: reactiveKafkaDeadLetterTopicProducer.process(record).then(Mono.error(ex)))
            .doFirst { log.error("Encountered [{}] during process of ${consumerName.eventType} {}", ex.message, record.key(), ex) }

    protected open fun getRetrySpec(record: ConsumerRecord<K, V>): Retry =
        Retry.fixedDelay(DEFAULT_RETRY_MAX_ATTEMPTS, Duration.ofSeconds(1))
            .doAfterRetry {
                log.warn("Retrying #{} processing ${consumerName.eventType} {} due to {}", it.totalRetries(), record.key(), it.failure().message, it.failure())
            }
            .onRetryExhaustedThrow { _, signal -> signal.failure() }

    abstract fun handle(record: ReceiverRecord<K, V>): Mono<Void>
}
