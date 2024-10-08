package inc.evil.bootiful_reactive_kafka.config.kafka

enum class KafkaConsumerName(val eventType: String) {
    DEFAULT("Unknown"), SESSION_STATE_UPDATE(String::class.java.simpleName)
}
