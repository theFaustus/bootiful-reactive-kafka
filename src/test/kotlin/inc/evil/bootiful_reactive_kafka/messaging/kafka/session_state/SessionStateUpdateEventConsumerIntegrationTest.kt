package inc.evil.bootiful_reactive_kafka.messaging.kafka.session_state

import inc.evil.bootiful_reactive_kafka.common.AbstractTestcontainersTest
import inc.evil.bootiful_reactive_kafka.common.ComponentTest
import inc.evil.bootiful_reactive_kafka.common.RunSql
import inc.evil.bootiful_reactive_kafka.service.SessionStateUpdateEventAuditService
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
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
class SessionStateUpdateEventConsumerIntegrationTest : AbstractTestcontainersTest() {

    @SpyBean
    private lateinit var sessionStateUpdateEventAuditService: SessionStateUpdateEventAuditService

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    @Value("\${spring.kafka.consumers.SESSION-STATE-UPDATE.topic}")
    lateinit var topicName: String

    @Test
    @RunSql(["/db-data/session-state-update-events.sql"])
    fun consume_withValidSessionStateUpdate_savesSessionStateInDatabase() {
        val userId = "user_987"
        val sessionState = "ACTIVE"
        assertThat(sessionStateUpdateEventAuditService.findByUserId(userId).collectList().block()).hasSize(1)

        kafkaTemplate.send(topicName, userId, sessionState).get()

        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted {
                assertThat(sessionStateUpdateEventAuditService.findByUserId(userId).collectList().block()).hasSize(2)
                verify(sessionStateUpdateEventAuditService).audit(userId, sessionState)
            }
    }

    @Test
    @RunSql(["/db-data/session-state-update-events.sql"])
    fun consume_withInvalidSessionStateUpdate_retriesAndSavesSessionStateInDatabase(logs: CapturedOutput) {
        val userId = "user_987"
        val sessionState = "ACTIVE"
        assertThat(sessionStateUpdateEventAuditService.findByUserId(userId).collectList().block()).hasSize(1)

        doAnswer { throw RuntimeException("Oops, something happened!") }
            .doAnswer { throw RuntimeException("Again? Again! Something happened!") }
            .doCallRealMethod()
            .whenever(sessionStateUpdateEventAuditService).audit(userId, sessionState)

        kafkaTemplate.send(topicName, userId, sessionState).get()

        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted {
                assertThat(sessionStateUpdateEventAuditService.findByUserId(userId).collectList().block()).hasSize(2)
                assertThat(logs.out).contains("Retrying #0 processing String user_987")
                assertThat(logs.out).contains("Retrying #1 processing String user_987")
                verify(sessionStateUpdateEventAuditService, times(3)).audit(userId, sessionState)
            }
    }

    @AfterEach
    fun reset() {
        LogManager.getLogManager().readConfiguration()
    }

}

