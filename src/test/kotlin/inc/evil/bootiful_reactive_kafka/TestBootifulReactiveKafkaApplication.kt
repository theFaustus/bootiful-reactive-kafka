package inc.evil.bootiful_reactive_kafka

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<BootifulReactiveKafkaApplication>().with(TestcontainersConfiguration::class).run(*args)
}
