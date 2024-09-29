package inc.evil.bootiful_reactive_kafka.web.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import inc.evil.bootiful_reactive_kafka.common.annotations.NoArgsConstructor
import java.net.InetAddress
import java.time.LocalDateTime

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
data class PongResponse(
    val message: String,
    val timestamp: String = LocalDateTime.now().toString(),
    val threadName: String = Thread.currentThread().name,
    val serverInstance: String = InetAddress.getLocalHost().hostName,
)
