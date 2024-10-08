package inc.evil.bootiful_reactive_kafka.messaging.kafka.log_event

import inc.evil.bootiful_reactive_kafka.common.AbstractTestcontainersTest
import inc.evil.bootiful_reactive_kafka.common.ComponentTest
import inc.evil.bootiful_reactive_kafka.common.RunSql
import inc.evil.bootiful_reactive_kafka.messaging.kafka.log_event.model.LoggedOutEvent
import inc.evil.bootiful_reactive_kafka.service.LogEventAuditService
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.kafka.core.KafkaTemplate
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.logging.LogManager

@ComponentTest
@ExtendWith(OutputCaptureExtension::class)
class LoggedOutEventConsumerIntegrationTest : AbstractTestcontainersTest() {

    @SpyBean
    private lateinit var logEventAuditService: LogEventAuditService

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    @Value("\${spring.kafka.consumers.LOGGED-OUT-EVENT.topic}")
    lateinit var topicName: String

    @Test
    @RunSql(["/db-data/log-events.sql"])
    fun consume_withValidLoggedOutEvent_savesLogEventInDatabase() {
        val userId = "backend_boss"
        //language=json
        val loggedInEvent = """
            {
              "userId": "backend_boss",
              "timestamp": "2024-10-08T17:00:00.456Z",
              "ipAddress": "1.1.1.1",
              "deviceType": "DESKTOP",
              "sessionId": "sessionId456",
              "logoutReason": "USER_INITIATED",
              "browser": "Firefox"
            }
        """.trimIndent()
        assertThat(logEventAuditService.findByUserId(userId).collectList().block()).hasSize(1)

        kafkaTemplate.send(topicName, userId, loggedInEvent).get()

        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted {
                assertThat(logEventAuditService.findByUserId(userId).collectList().block()).hasSize(2)
                val loggedInEventArgumentCaptor = argumentCaptor<LoggedOutEvent>()
                verify(logEventAuditService).audit(loggedInEventArgumentCaptor.capture())
                assertThat(loggedInEventArgumentCaptor.firstValue.userId).isEqualTo(userId)
                assertThat(loggedInEventArgumentCaptor.firstValue.ipAddress).isEqualTo("1.1.1.1")
                assertThat(loggedInEventArgumentCaptor.firstValue.deviceType).isEqualTo("DESKTOP")
                assertThat(loggedInEventArgumentCaptor.firstValue.browser).isEqualTo("Firefox")
                assertThat(loggedInEventArgumentCaptor.firstValue.logoutReason).isEqualTo("USER_INITIATED")
                assertThat(loggedInEventArgumentCaptor.firstValue.sessionId).isEqualTo("sessionId456")
            }
    }

    @Test
    @RunSql(["/db-data/log-events.sql"])
    fun consume_withInvalidLoggedOutEvent_retriesAndSavesLogEventInDatabase(logs: CapturedOutput) {
        val userId = "backend_boss"
        //language=json
        val loggedInEvent = """
{
              "userId": "backend_boss",
              "timestamp": "2024-10-08T17:00:00.456Z",
              "ipAddress": "1.1.1.1",
              "deviceType": "DESKTOP",
              "sessionId": "sessionId456",
              "logoutReason": "USER_INITIATED",
              "browser": "Firefox"
            }
        """.trimIndent()
        assertThat(logEventAuditService.findByUserId(userId).collectList().block()).hasSize(1)

        doAnswer { throw RuntimeException("Oops, something happened!") }
            .doAnswer { throw RuntimeException("Again? Again! Something happened!") }
            .doCallRealMethod()
            .whenever(logEventAuditService).audit(anyOrNull<LoggedOutEvent>())

        kafkaTemplate.send(topicName, userId, loggedInEvent).get()

        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted {
                assertThat(logEventAuditService.findByUserId(userId).collectList().block()).hasSize(2)
                assertThat(logs.out).contains("Retrying #0 processing LoggedOutEvent")
                assertThat(logs.out).contains("Retrying #1 processing LoggedOutEvent")
                val loggedInEventArgumentCaptor = argumentCaptor<LoggedOutEvent>()
                verify(logEventAuditService, times(3)).audit(loggedInEventArgumentCaptor.capture())
                assertThat(loggedInEventArgumentCaptor.firstValue.userId).isEqualTo(userId)
                assertThat(loggedInEventArgumentCaptor.firstValue.ipAddress).isEqualTo("1.1.1.1")
                assertThat(loggedInEventArgumentCaptor.firstValue.deviceType).isEqualTo("DESKTOP")
                assertThat(loggedInEventArgumentCaptor.firstValue.browser).isEqualTo("Firefox")
                assertThat(loggedInEventArgumentCaptor.firstValue.logoutReason).isEqualTo("USER_INITIATED")
                assertThat(loggedInEventArgumentCaptor.firstValue.sessionId).isEqualTo("sessionId456")
            }
    }

    @AfterEach
    fun reset() {
        LogManager.getLogManager().readConfiguration()
    }

}
