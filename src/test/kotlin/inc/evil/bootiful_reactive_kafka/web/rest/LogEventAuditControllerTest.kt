package inc.evil.bootiful_reactive_kafka.web.rest

import inc.evil.bootiful_reactive_kafka.common.fixtures.LogEventViewFixture
import inc.evil.bootiful_reactive_kafka.service.LogEventAuditService
import inc.evil.bootiful_reactive_kafka.web.dto.LogEventView
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import kotlin.test.Test

@WebFluxTest(controllers = [LogEventAuditController::class])
class LogEventAuditControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var logEventAuditService: LogEventAuditService

    @Test
    fun getUserEvents_withValidUserId_returnsEvents() {
        val userId = "testUser"
        val logEventView = LogEventViewFixture.of()
        whenever(logEventAuditService.findUserEvents(userId)).thenReturn(Flux.just(logEventView))

        webTestClient.get()
            .uri("/api/v1/log-events?userId=$userId")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(LogEventView::class.java)
            .hasSize(1)
            .contains(logEventView)
    }
}
