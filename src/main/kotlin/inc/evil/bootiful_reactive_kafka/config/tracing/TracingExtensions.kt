package inc.evil.bootiful_reactive_kafka.config.tracing

import io.micrometer.observation.ObservationRegistry
import reactor.core.observability.micrometer.Micrometer
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

inline fun <reified T> Flux<T>.observe(
    observationRegistry: ObservationRegistry,
    name: String = Throwable().stackTrace[1].methodName,
    tags: () -> Map<String, Any> = { emptyMap() }
): Flux<T> =
    tags().entries.fold(name(name)) { acc, (key, value) -> acc.tag(key, value.toString()) }.tap(Micrometer.observation(observationRegistry))

inline fun <reified T> Mono<T>.observe(
    observationRegistry: ObservationRegistry,
    name: String = Throwable().stackTrace[1].methodName,
    tags: () -> Map<String, Any> = { emptyMap() }
): Mono<T> =
    tags().entries.fold(name(name)) { acc, (key, value) -> acc.tag(key, value.toString()) }.tap(Micrometer.observation(observationRegistry))
