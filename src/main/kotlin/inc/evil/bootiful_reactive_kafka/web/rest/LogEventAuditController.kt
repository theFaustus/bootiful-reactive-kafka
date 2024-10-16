package inc.evil.bootiful_reactive_kafka.web.rest

import inc.evil.bootiful_reactive_kafka.service.LogEventService
import inc.evil.bootiful_reactive_kafka.web.dto.LogEventView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/v1/log-events")
class LogEventAuditController(private val logEventService: LogEventService) {

    @GetMapping
    fun getUserEvents(@RequestParam(required = true) userId: String): Flux<LogEventView> =
        logEventService.findByUserId(userId)
}
