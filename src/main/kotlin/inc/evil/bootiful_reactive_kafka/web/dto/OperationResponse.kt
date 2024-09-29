package inc.evil.bootiful_reactive_kafka.web.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import inc.evil.bootiful_reactive_kafka.common.annotations.NoArgsConstructor

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
data class OperationResponse(
    val success: Boolean,
    val messages: Set<String>? = null,
) {
    companion object {
        fun success(message: String): OperationResponse = OperationResponse(true, setOf(message))
        fun error(message: String?): OperationResponse = OperationResponse(false, message?.let { setOf(it) })
        fun error(messages: Set<String>?): OperationResponse = OperationResponse(false, messages)
    }
}
