package inc.evil.bootiful_reactive_kafka.config.kafka.producer

import org.apache.kafka.clients.producer.ProducerRecord
import org.checkerframework.checker.units.qual.K
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord
import reactor.kafka.sender.SenderRecord
import reactor.kafka.sender.SenderResult

@Component
class ReactiveKafkaDeadLetterTopicProducer(kafkaSenderOptionsFactory: KafkaSenderOptionsFactory) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    private val kafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, String> by lazy {
        ReactiveKafkaProducerTemplate(kafkaSenderOptionsFactory.createDltSenderOptions<String, String>())
    }

    fun <K,V> process(record: ReceiverRecord<K, V>): Mono<SenderResult<String>> =
        kafkaProducerTemplate.send(Mono.just<SenderRecord<String, String, String>>(record.asSenderRecord()))
            .doOnNext { log.info("Dead-letter topic correlationMetadata: {}", it.correlationMetadata()) }
            .next()
            .doOnSuccess { log.debug("Published message with key={} and value={} to dead-letter topic", record.key(), record.value()) }
            .doOnError { log.error("Error publishing message with key={} and value={} to dead-letter topic", record.key(), record.value(), it) }

    private fun <K,V> ReceiverRecord<K, V>.asSenderRecord(): SenderRecord<String, String, String> {
        val deadLetterTopic = "${topic()}-dlt"
        val producerRecord: ProducerRecord<String, String> = ProducerRecord<String, String>(deadLetterTopic, key().toString(), value().toString())
        log.debug("Computed producer record with key={} and value={} and dead-letter topic={}", key(), value(), deadLetterTopic)
        return SenderRecord.create(producerRecord, producerRecord.key())
    }
}
