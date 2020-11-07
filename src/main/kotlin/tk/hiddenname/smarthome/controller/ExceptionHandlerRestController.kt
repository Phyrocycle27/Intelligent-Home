package tk.hiddenname.smarthome.controller


import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import tk.hiddenname.smarthome.exception.invalid.InvalidProcessingActionException
import tk.hiddenname.smarthome.exception.invalid.InvalidTriggerActionException
import tk.hiddenname.smarthome.model.ApiError
import tk.hiddenname.smarthome.model.CustomFieldError
import java.time.LocalDateTime

@RestControllerAdvice
open class ExceptionHandlerRestController {

    @Suppress("unused")
    private val log = LoggerFactory.getLogger(ExceptionHandlerRestController::class.java)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun catchMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ApiError {
        return processFieldErrors(ex.bindingResult.fieldErrors)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonMappingException::class)
    fun catchInvalidTypeIdException(ex: JsonMappingException): ApiError {
        val message = when(ex.cause) {
            is InvalidTriggerActionException,
            is InvalidProcessingActionException -> ex.cause?.message ?: ""
            is JsonParseException -> "Json parse error. ${ex.cause?.message ?: ""}"
            else -> {
                ex.printStackTrace()
                ""
            }
        }

        return ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidTypeIdException::class)
    fun catchInvalidTypeIdException(ex: InvalidTypeIdException): ApiError {
        return ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Type-id field is missing")
    }

    private fun processFieldErrors(fieldErrors: List<FieldError>): ApiError {
        val error = ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Validation error")

        fieldErrors.forEach {
            val fieldError = CustomFieldError(it.defaultMessage ?: "", it.objectName, it.field)
            error.addFieldError(fieldError)
        }

        return error
    }
}