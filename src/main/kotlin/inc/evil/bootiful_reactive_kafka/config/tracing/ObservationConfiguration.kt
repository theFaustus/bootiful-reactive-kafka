package inc.evil.bootiful_reactive_kafka.config.tracing

import io.micrometer.observation.ObservationHandler
import io.micrometer.observation.ObservationRegistry
import io.micrometer.tracing.Tracer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test&!integration")
class ObservationConfiguration(private val observationHandlers: Set<ObservationHandler<*>>) {
    @Bean
    fun observationRegistry(tracer: Tracer): ObservationRegistry {
        val observationRegistry = ObservationRegistry.create()
        observationHandlers.forEach(observationRegistry.observationConfig()::observationHandler)
        return observationRegistry
    }
}
