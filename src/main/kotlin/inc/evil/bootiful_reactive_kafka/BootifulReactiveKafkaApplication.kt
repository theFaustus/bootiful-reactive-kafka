package inc.evil.bootiful_reactive_kafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication
class BootifulReactiveKafkaApplication

fun main(args: Array<String>) {
	runApplication<BootifulReactiveKafkaApplication>(*args)
	Hooks.enableAutomaticContextPropagation()
}
