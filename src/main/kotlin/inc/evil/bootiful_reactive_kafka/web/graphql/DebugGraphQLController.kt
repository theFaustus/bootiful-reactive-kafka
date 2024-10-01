package inc.evil.bootiful_reactive_kafka.web.graphql

import inc.evil.bootiful_reactive_kafka.web.dto.PongResponse
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class DebugGraphQLController {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @QueryMapping
    suspend fun ping() = Mono.just(PongResponse("pong")).doOnSuccess { log.debug("Requested ping") }.awaitSingleOrNull()
}
