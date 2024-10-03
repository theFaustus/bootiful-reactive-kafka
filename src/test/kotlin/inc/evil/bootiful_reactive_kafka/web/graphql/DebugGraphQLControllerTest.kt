package inc.evil.bootiful_reactive_kafka.web.graphql

import inc.evil.bootiful_reactive_kafka.common.AbstractTestcontainersTest
import inc.evil.bootiful_reactive_kafka.common.ComponentTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.test.tester.GraphQlTester
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@ComponentTest
class DebugGraphQLControllerTest : AbstractTestcontainersTest() {

    @Autowired
    private lateinit var graphQlTester: GraphQlTester

    @Test
    fun ping_returnsPongResponse() {
        //language=graphql
        val query = """
            query  {
                ping {
                    message
                    serverInstance
                    threadName
                    timestamp
                }
            }
        """.trimIndent()

        graphQlTester.document(query)
            .execute()
            .path("ping.message").entity(String::class.java).isEqualTo("pong")
            .path("ping.serverInstance").entity(String::class.java).satisfies { assertThat(it).isNotBlank() }
            .path("ping.threadName").entity(String::class.java).satisfies { assertThat(it).isNotBlank() }
            .path("ping.timestamp").entity(LocalDateTime::class.java).satisfies { assertThat(it).isCloseTo(LocalDateTime.now(), within(30, ChronoUnit.SECONDS)) }
    }
}
