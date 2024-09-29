package inc.evil.bootiful_reactive_kafka.web

import inc.evil.bootiful_reactive_kafka.web.dto.PongResponse
import io.micrometer.tracing.annotation.NewSpan
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/debug")
class DebugController {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @NewSpan
    @GetMapping("/ping")
    fun ping()= Mono.just(PongResponse("pong")).doOnSuccess { log.debug("Requested ping") }
}
