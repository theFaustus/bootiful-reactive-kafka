package inc.evil.bootiful_reactive_kafka.repo

import inc.evil.bootiful_reactive_kafka.common.AbstractTestcontainersTest
import inc.evil.bootiful_reactive_kafka.common.RunSql
import inc.evil.bootiful_reactive_kafka.common.TestcontainersIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@TestcontainersIntegrationTest
class LogEventAuditRepositoryTest : AbstractTestcontainersTest() {

    @Autowired
    private lateinit var repo: LogEventAuditRepository

    @Test
    @RunSql(["/db-data/log-events.sql"])
    fun findByUserId() {
        assertThat(repo.findByUserId("codeMasterX").collectList().block()).isNotEmpty.hasSize(1)
    }

}
