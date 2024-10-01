package inc.evil.bootiful_reactive_kafka.config.tracing

import io.micrometer.tracing.Tracer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain

@Configuration(proxyBeanMethods = false)
class TracerConfiguration {

    @Bean
    fun traceIdInResponseFilter(tracer: Tracer): WebFilter =
        WebFilter { exchange: ServerWebExchange, chain: WebFilterChain ->
            tracer.currentSpan()?.let { exchange.response.headers.add("traceId", it.context().traceId()) }
            chain.filter(exchange)
        }
}
