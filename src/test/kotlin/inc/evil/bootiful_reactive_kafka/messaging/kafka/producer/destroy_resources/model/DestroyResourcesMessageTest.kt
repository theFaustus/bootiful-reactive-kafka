package inc.evil.bootiful_reactive_kafka.messaging.kafka.producer.destroy_resources.model

import inc.evil.bootiful_reactive_kafka.common.fixtures.LoggedOutEventFixture
import inc.evil.bootiful_reactive_kafka.domain.LogoutReason
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DestroyResourcesMessageTest {

    @Test
    fun from_withValidLoggedOutEvent_returnsCorrectDestroyResourcesMessage() {
        val loggedOutEvent = LoggedOutEventFixture.of()

        val result = DestroyResourcesMessage.from(loggedOutEvent)

        assertThat(result.userId).isEqualTo(loggedOutEvent.userId)
        assertThat(result.resources).isEqualTo(LogoutReason.valueOf(loggedOutEvent.logoutReason).resourcesToDestroy)
    }
}
