package inc.evil.bootiful_reactive_kafka.common.fixtures

import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.session_state.model.AttackMethod
import inc.evil.bootiful_reactive_kafka.messaging.kafka.consumer.session_state.model.SessionHackAttemptEvent

class SessionHackAttemptEventFixture {
    companion object {
        fun of(
            userId: String = "SpongeBobSquarePants",
            ipAddress: String = "1.1.1.1",
            sessionId: String? = "Nautilus Browser",
        ) =
            SessionHackAttemptEvent.newBuilder()
                .setSessionId(sessionId)
                .setUserId(userId)
                .setAttackMethod(AttackMethod.MAN_IN_THE_MIDDLE)
                .setIpAddress(ipAddress)
                .build()
    }
}
