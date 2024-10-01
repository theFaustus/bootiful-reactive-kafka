package inc.evil.bootiful_reactive_kafka.common

import inc.evil.bootiful_reactive_kafka.config.db.R2dbcConfiguration
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import

@DataR2dbcTest
@Target(AnnotationTarget.CLASS)
@ExtendWith(RunSqlExtension::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(R2dbcConfiguration::class)
@Tag("integration-test")
annotation class TestcontainersIntegrationTest()
