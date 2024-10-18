package inc.evil.bootiful_reactive_kafka.common

import io.r2dbc.spi.ConnectionFactory
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ScriptUtils
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import reactor.core.publisher.Mono


@Tag("integration-test")
abstract class AbstractTestcontainersTest {

    @Autowired
    lateinit var connectionFactory: ConnectionFactory

    fun executeScriptBlocking(sqlScript: String) {
        Mono.from(connectionFactory.create())
            .flatMap<Any> { connection -> ScriptUtils.executeSqlScript(connection, ClassPathResource(sqlScript)) }.block()
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)

        private val postgres: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:13.3"))
            .apply {
                this.withDatabaseName("testDb")
                    .withUsername("root")
                    .withPassword("123456")
                    .withReuse(true)
            }

        private val network: Network = Network.newNetwork()
        private val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"))
            .withNetwork(network)
            .withReuse(true)

        private val schemaRegistry: GenericContainer<*> = GenericContainer(DockerImageName.parse("confluentinc/cp-schema-registry:7.5.2"))
            .withNetwork(network)
            .withExposedPorts(8081)
            .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
            .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
            .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", "PLAINTEXT://${kafka.networkAliases[0]}:9092")
            .waitingFor(Wait.forHttp("/subjects").forStatusCode(200))
            .withReuse(true)

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url", Companion::r2dbcUrl)
            registry.add("spring.r2dbc.username", postgres::getUsername)
            registry.add("spring.r2dbc.password", postgres::getPassword)
            registry.add("spring.flyway.url", postgres::getJdbcUrl)
            registry.add("spring.kafka.bootstrap-servers") { bootstrapServers() }
            registry.add("spring.kafka.consumers.DEFAULT.properties.bootstrap.servers") { kafka.bootstrapServers }
            registry.add("spring.kafka.consumers.DEFAULT.properties.schema.registry.url") { schemaRegistryUrl() }
            registry.add("spring.kafka.consumers.DEFAULT.properties.auto.offset.reset") { "earliest" }
            registry.add("spring.kafka.producers.DEFAULT.properties.bootstrap.servers") { kafka.bootstrapServers }
            registry.add("spring.kafka.producers.DEFAULT.properties.schema.registry.url") { schemaRegistryUrl() }
        }

        fun schemaRegistryUrl() = "http://${schemaRegistry.host}:${schemaRegistry.firstMappedPort}"

        fun bootstrapServers() = kafka.bootstrapServers

        private fun r2dbcUrl(): String = "r2dbc:postgresql://${postgres.host}:${postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT)}/${postgres.databaseName}"

        @JvmStatic
        @BeforeAll
        internal fun setUp() {
            postgres.start()
            log.info("Testcontainers -> PostgresSQL DB started on [${r2dbcUrl()}] with user:root and password:123456")
            kafka.start()
            log.info("Testcontainers -> Kafka started with bootstrap.servers=${kafka.bootstrapServers}")
            schemaRegistry.start()
            log.info("Testcontainers -> Kafka Schema Registry started with url=${schemaRegistryUrl()}")
        }
    }
}
