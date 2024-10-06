package inc.evil.bootiful_reactive_kafka.repo

import inc.evil.bootiful_reactive_kafka.common.AbstractTestcontainersTest
import inc.evil.bootiful_reactive_kafka.common.RunSql
import inc.evil.bootiful_reactive_kafka.common.TestcontainersIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@TestcontainersIntegrationTest
class SessionStateUpdateEventAuditRepositoryTest : AbstractTestcontainersTest() {

    @Autowired
    private lateinit var repo: SessionStateUpdateEventAuditRepository

    @Test
    @RunSql(["/db-data/session-state-update-events.sql"])
    fun findByUserId() {
        val sessionStateUpdateEventAudits = repo.findByUserId("codeMasterX").collectList().block()
        assertThat(sessionStateUpdateEventAudits).isNotEmpty.hasSize(2)
    }

}
