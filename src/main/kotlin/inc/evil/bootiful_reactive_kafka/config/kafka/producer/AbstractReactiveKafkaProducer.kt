package inc.evil.bootiful_reactive_kafka.config.kafka.producer

import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.core.publisher.Mono

abstract class AbstractReactiveKafkaProducer<K : Any, V : Any>(private val producerName: KafkaProducerName) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var kafkaSenderOptionsFactory: KafkaSenderOptionsFactory

    private val kafkaProducerTemplate: ReactiveKafkaProducerTemplate<K, V> by lazy {
        ReactiveKafkaProducerTemplate(kafkaSenderOptionsFactory.createSenderOptions<K, V>(producerName))
    }

    open fun send(key: K, value: V, headers: Iterable<Header> = mutableSetOf()): Mono<Void> =
        Mono.just(ProducerRecord(kafkaSenderOptionsFactory.getSenderTopic(producerName), null, key, value, headers))
            .flatMap { record ->
                kafkaProducerTemplate.send(record)
                    .doOnSuccess { log.debug("{} published message with key={} and value={}", producerName, key, value) }
                    .doOnError { log.error("Encountered [{}] during sending of ${producerName.eventType}", it.message, it) }
                    .then()
            }

}
