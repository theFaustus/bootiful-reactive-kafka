package inc.evil.bootiful_reactive_kafka

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks


@SpringBootApplication
class BootifulReactiveKafkaApplication {

    @PostConstruct
    fun init() {
        Hooks.enableAutomaticContextPropagation()
    }
}

fun main(args: Array<String>) {
    runApplication<BootifulReactiveKafkaApplication>(*args)
}
