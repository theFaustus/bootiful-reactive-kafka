package inc.evil.bootiful_reactive_kafka.web.rest

import inc.evil.bootiful_reactive_kafka.web.dto.PongResponse
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@WebFluxTest(controllers = [DebugController::class])
class DebugControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun ping_returnsPongResponse() {
        webTestClient.get()
            .uri("/api/v1/debug/ping")
            .exchange()
            .expectStatus().isOk
            .expectBody(PongResponse::class.java)
            .returnResult()
            .responseBody
            ?.let {
                assertThat(it.message).isEqualTo("pong")
                assertThat(it.message).isNotBlank()
                assertThat(it.threadName).isNotBlank()
                assertThat(LocalDateTime.parse(it.timestamp)).isCloseTo(LocalDateTime.now(), within(30, ChronoUnit.SECONDS))
            }
    }

}
