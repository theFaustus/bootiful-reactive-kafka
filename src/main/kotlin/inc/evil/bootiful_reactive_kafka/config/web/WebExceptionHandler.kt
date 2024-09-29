package inc.evil.bootiful_reactive_kafka.config.web

import inc.evil.bootiful_reactive_kafka.common.exception.NotFoundException
import inc.evil.bootiful_reactive_kafka.web.dto.OperationResponse
import jakarta.validation.ConstraintViolationException
import jakarta.validation.ValidationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@Order
@ControllerAdvice(basePackages = ["inc.evil.bootiful_reactive_kafka.config.web.rest"])
class WebExceptionHandler {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @ExceptionHandler(Exception::class)
    fun onException(e: Exception): ResponseEntity<OperationResponse> {
        log.error("Exception while handling request ${e.message}", e)
        val errorModel = OperationResponse.error(setOf(e.message ?: "n/a"))
        return ResponseEntity.internalServerError().body(errorModel)
    }

    @ExceptionHandler(ValidationException::class)
    fun onValidationException(e: ValidationException): ResponseEntity<OperationResponse> {
        log.warn("Validation error while handling request: ${e.message}", e)
        val errorModel = OperationResponse.error(setOf(e.message ?: "n/a"))
        return ResponseEntity.badRequest().body(errorModel)
    }

    @ExceptionHandler(NotFoundException::class)
    fun onNotFoundException(e: NotFoundException): ResponseEntity<OperationResponse> {
        log.warn("Entity not found: ${e.message}", e)
        val errorModel = OperationResponse.error(setOf(e.message ?: "n/a"))
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun onMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<OperationResponse> {
        log.warn("Exception while handling request: ${e.message}", e)
        val errorMessages = mutableSetOf<String>()
        e.bindingResult.allErrors.forEach { error ->
            if (error is FieldError) {
                errorMessages.add("Field '${error.field}' ${error.defaultMessage}, but value was [${error.rejectedValue}]")
            } else {
                errorMessages.add(error.codes.contentToString())
            }
        }
        val errorModel = OperationResponse.error(errorMessages)
        return ResponseEntity.badRequest().body(errorModel)
    }

    @ExceptionHandler(WebExchangeBindException::class)
    fun onWebExchangeBindException(e: WebExchangeBindException): ResponseEntity<OperationResponse> {
        log.warn("Exception while handling request: ${e.message}", e)
        val errorMessages = mutableSetOf<String>()
        e.bindingResult.allErrors.forEach { error ->
            if (error is FieldError) {
                errorMessages.add("Field '${error.field}' ${error.defaultMessage}, but value was [${error.rejectedValue}]")
            } else {
                errorMessages.add(error.codes.contentToString())
            }
        }
        val errorModel = OperationResponse.error(errorMessages)
        return ResponseEntity.badRequest().body(errorModel)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun onConstraintViolationException(e: ConstraintViolationException): ResponseEntity<OperationResponse> {
        log.warn("Exception while handling request: ${e.message}", e)
        val errorMessages = mutableSetOf<String>()
        e.constraintViolations.forEach { errorMessages.add("Field '${it.propertyPath}' ${it.message}, but value was [${it.invalidValue}]") }
        val errorModel = OperationResponse.error(errorMessages)
        return ResponseEntity.badRequest().body(errorModel)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun onMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<OperationResponse?>? {
        val parameter = e.parameter
        val message = "Parameter: '${parameter.parameterName}' is not valid. Value '${e.value}' could not be bound to type: '${parameter.parameterType.simpleName.lowercase()}'"
        log.warn("Exception while handling request: $message", e)
        val errorModel = OperationResponse.error(setOf(message))
        return ResponseEntity.badRequest().body(errorModel)
    }
}
