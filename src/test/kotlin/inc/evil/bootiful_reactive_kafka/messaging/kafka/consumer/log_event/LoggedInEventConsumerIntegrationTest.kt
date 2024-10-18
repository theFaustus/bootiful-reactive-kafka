package inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event

import inc.evil.bootiful_reactive_kafka.common.AbstractTestcontainersTest
import inc.evil.bootiful_reactive_kafka.common.ComponentTest
import inc.evil.bootiful_reactive_kafka.common.RunSql
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.log_event.model.LoggedInEvent
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.init_resources.InitResourcesMessageProducer
import inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.init_resources.model.InitCommandType
import inc.evil.bootiful_reactive_kafka.service.LogEventService
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.LogManager

@ComponentTest
@ExtendWith(OutputCaptureExtension::class)
class LoggedInEventConsumerIntegrationTest : AbstractTestcontainersTest() {

    @SpyBean
    private lateinit var logEventService: LogEventService

    @SpyBean
    private lateinit var initResourcesMessageProducer: InitResourcesMessageProducer

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    companion object {
        private val topicName = "test-topic-${UUID.randomUUID()}"

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.consumers.LOGGED-IN-EVENT.topic") { topicName }
        }
    }

    @Test
    @RunSql(["/db-data/log-events.sql"])
    fun consume_withValidLoggedInEvent_savesLogEventInDatabase() {
        val userId = "backend_boss"
        //language=json
        val loggedInEvent = """
            {
              "userId": "backend_boss",
              "timestamp": "2024-10-08T12:45:00.123Z",
              "ipAddress": "1.1.1.1",
              "deviceType": "DESKTOP",
              "browser": "Firefox",
              "loginMethod": "PASSWORD",
              "sessionId": "sessionId456"
            }
        """.trimIndent()
        assertThat(logEventService.findByUserId(userId).collectList().block()).hasSize(1)

        kafkaTemplate.send(topicName, userId, loggedInEvent).get()

        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted {
                assertThat(logEventService.findByUserId(userId).collectList().block()).hasSize(2)
                val loggedInEventArgumentCaptor = argumentCaptor<LoggedInEvent>()
                verify(logEventService).handle(loggedInEventArgumentCaptor.capture())
                assertThat(loggedInEventArgumentCaptor.firstValue.userId).isEqualTo(userId)
                assertThat(loggedInEventArgumentCaptor.firstValue.ipAddress).isEqualTo("1.1.1.1")
                assertThat(loggedInEventArgumentCaptor.firstValue.deviceType).isEqualTo("DESKTOP")
                assertThat(loggedInEventArgumentCaptor.firstValue.browser).isEqualTo("Firefox")
                assertThat(loggedInEventArgumentCaptor.firstValue.loginMethod).isEqualTo("PASSWORD")
                assertThat(loggedInEventArgumentCaptor.firstValue.sessionId).isEqualTo("sessionId456")
                verify(initResourcesMessageProducer).send(eq(userId), eq(InitCommandType.INIT.name), anyOrNull())
            }
    }

    @Test
    @RunSql(["/db-data/log-events.sql"])
    fun consume_withInvalidLoggedInEvent_retriesAndSavesLogEventInDatabase(logs: CapturedOutput) {
        val userId = "backend_boss"
        //language=json
        val loggedInEvent = """
            {
              "userId": "backend_boss",
              "timestamp": "2024-10-08T12:45:00.123Z",
              "ipAddress": "1.1.1.1",
              "deviceType": "DESKTOP",
              "browser": "Firefox",
              "loginMethod": "PASSWORD",
              "sessionId": "sessionId456"
            }
        """.trimIndent()
        assertThat(logEventService.findByUserId(userId).collectList().block()).hasSize(1)

        doAnswer { throw RuntimeException("Oops, something happened!") }
            .doAnswer { throw RuntimeException("Again? Again! Something happened!") }
            .doCallRealMethod()
            .whenever(logEventService).handle(anyOrNull<LoggedInEvent>())

        kafkaTemplate.send(topicName, userId, loggedInEvent).get()

        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted {
                assertThat(logEventService.findByUserId(userId).collectList().block()).hasSize(2)
                assertThat(logs.out).contains("Retrying #0 processing LoggedInEvent")
                assertThat(logs.out).contains("Retrying #1 processing LoggedInEvent")
                val loggedInEventArgumentCaptor = argumentCaptor<LoggedInEvent>()
                verify(logEventService, times(3)).handle(loggedInEventArgumentCaptor.capture())
                assertThat(loggedInEventArgumentCaptor.firstValue.userId).isEqualTo(userId)
                assertThat(loggedInEventArgumentCaptor.firstValue.ipAddress).isEqualTo("1.1.1.1")
                assertThat(loggedInEventArgumentCaptor.firstValue.deviceType).isEqualTo("DESKTOP")
                assertThat(loggedInEventArgumentCaptor.firstValue.browser).isEqualTo("Firefox")
                assertThat(loggedInEventArgumentCaptor.firstValue.loginMethod).isEqualTo("PASSWORD")
                assertThat(loggedInEventArgumentCaptor.firstValue.sessionId).isEqualTo("sessionId456")
                verify(initResourcesMessageProducer).send(eq(userId), eq(InitCommandType.INIT.name), anyOrNull())
            }
    }

    @AfterEach
    fun reset() {
        LogManager.getLogManager().readConfiguration()
    }

}

