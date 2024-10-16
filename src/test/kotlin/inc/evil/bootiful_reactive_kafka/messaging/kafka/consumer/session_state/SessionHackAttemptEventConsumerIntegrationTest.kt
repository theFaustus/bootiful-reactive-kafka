package inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.session_state

import inc.evil.bootiful_reactive_kafka.common.AbstractTestcontainersTest
import inc.evil.bootiful_reactive_kafka.common.ComponentTest
import inc.evil.bootiful_reactive_kafka.common.RunSql
import inc.evil.bootiful_reactive_kafka.common.fixtures.SessionHackAttemptEventFixture
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.session_state.model.SessionHackAttemptEvent
import inc.evil.bootiful_reactive_kafka.service.SessionStateUpdateEventAuditService
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.logging.LogManager

@ComponentTest
@ExtendWith(OutputCaptureExtension::class)
class SessionHackAttemptEventConsumerIntegrationTest : AbstractTestcontainersTest() {

    @SpyBean
    private lateinit var sessionStateUpdateEventAuditService: SessionStateUpdateEventAuditService

    @Value("\${spring.kafka.consumers.SESSION_HACK_ATTEMPT.topic}")
    private lateinit var topicName: String

    @Test
    @RunSql(["/db-data/session-state-update-events.sql"])
    fun consume_withValidSessionHackAttemptEvent_savesSessionStateInDatabase() {
        val userId = "user_987"
        val sessionHackAttemptEvent = SessionHackAttemptEventFixture.of()
        assertThat(sessionStateUpdateEventAuditService.findByUserId(userId).collectList().block()).hasSize(1)

        getKafkaProducer().send(ProducerRecord(topicName, userId, sessionHackAttemptEvent)).get()

        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted {
                assertThat(sessionStateUpdateEventAuditService.findByUserId(userId).collectList().block()).hasSize(2)
                verify(sessionStateUpdateEventAuditService).audit(userId, sessionHackAttemptEvent)
            }
    }

    @Test
    @RunSql(["/db-data/session-state-update-events.sql"])
    fun consume_withErrorProcessing_retriesAndSavesSessionStateInDatabase(logs: CapturedOutput) {
        val userId = "user_987"
        val sessionHackAttemptEvent = SessionHackAttemptEventFixture.of()
        assertThat(sessionStateUpdateEventAuditService.findByUserId(userId).collectList().block()).hasSize(1)

        doAnswer { throw RuntimeException("Oops, something happened!") }
            .doAnswer { throw RuntimeException("Again? Again! Something happened!") }
            .doCallRealMethod()
            .whenever(sessionStateUpdateEventAuditService).audit(eq(userId), anyOrNull<SessionHackAttemptEvent>())

        getKafkaProducer().send(ProducerRecord(topicName, userId, sessionHackAttemptEvent)).get()

        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted {
                assertThat(sessionStateUpdateEventAuditService.findByUserId(userId).collectList().block()).hasSize(2)
                assertThat(logs.out).contains("Retrying #0 processing SessionHackAttemptEvent user_987")
                assertThat(logs.out).contains("Retrying #1 processing SessionHackAttemptEvent user_987")
                verify(sessionStateUpdateEventAuditService, times(3)).audit(userId, sessionHackAttemptEvent)
            }
    }

    private fun getKafkaProducer(): KafkaProducer<String, SessionHackAttemptEvent> {
        val properties = mapOf(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers())
        val valueSerializer = SpecificAvroSerializer<SessionHackAttemptEvent>()
        valueSerializer.configure(mapOf(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG to schemRegistryUrl()), false)
        return KafkaProducer(properties, StringSerializer(), valueSerializer)
    }

    @AfterEach
    fun reset() {
        LogManager.getLogManager().readConfiguration()
    }

}

