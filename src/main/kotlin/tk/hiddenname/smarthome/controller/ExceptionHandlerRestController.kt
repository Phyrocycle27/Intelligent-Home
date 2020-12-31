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
import tk.hiddenname.smarthome.exception.ApiException
import tk.hiddenname.smarthome.exception.exist.*
import tk.hiddenname.smarthome.exception.invalid.*
import tk.hiddenname.smarthome.exception.not_found.*
import tk.hiddenname.smarthome.exception.not_specified.*
import tk.hiddenname.smarthome.exception.support.*
import tk.hiddenname.smarthome.model.error.*
import java.time.LocalDateTime

@RestControllerAdvice
class ExceptionHandlerRestController {

    @Suppress("unused")
    private val log = LoggerFactory.getLogger(ExceptionHandlerRestController::class.java)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun catchMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ValidationError {
        return processFieldErrors(ex.bindingResult.fieldErrors)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonMappingException::class)
    fun catchJsonMappingException(ex: JsonMappingException): ApiError {
        val message = when(ex.cause) {
            is InvalidSignalTypeException,
            is InvalidTimetableModeException,
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AreaNotFoundException::class, DeviceNotFoundException::class, ProcessorNotFoundException::class,
            SensorNotFoundException::class, TaskNotFoundException::class, TriggerNotFoundException::class,
            GpioPinNotFoundException::class)
    fun catchNotFoundException(ex: ApiException): ApiError {
        return ApiError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.message ?: "Not found exception")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GpioNotSpecifiedException::class, GpioPinNotSpecifiedException::class,
            HardwareIdNotSpecifiedException::class, SignalTypeNotSpecifiedException::class,
            SignalValueNotSpecifiedException::class, TriggerObjectPropertyNotSpecifiedException::class,
            ProcessingObjectPropertyNotSpecifiedException::class)
    fun catchNotSpecifiedException(ex: ApiException): ApiError {
        return ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                ex.message ?: "Not specified exception")
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(GpioModeNotSupportsException::class, GpioModeNotSupportsWithSignalTypeException::class,
            NoSuchListenerException::class, NoSuchProcessorException::class, PinSignalSupportException::class,
            UnsupportedSignalTypeException::class)
    fun catchUnsupportedException(ex: ApiException): ApiError {
        return ApiError(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
                ex.message ?: "Not supports exception")
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(GpioPinBusyException::class, ProcessorExistsException::class,
            TriggerExistsException::class)
    fun catchExistingConflictException(ex: ApiException): ApiError {
        return ApiError(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
                ex.message ?: "Existing conflict exception")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidSignalTypeException::class)
    fun catchInvalidSignalTypeException(ex: ApiException): ApiError {
        return ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                ex.message ?: "Invalid signal type")
    }

    private fun processFieldErrors(fieldErrors: List<FieldError>): ValidationError {
        val error = ValidationError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Validation error")

        fieldErrors.forEach {
            val fieldError = CustomFieldError(it.defaultMessage ?: "", it.objectName, it.field)
            error.addFieldError(fieldError)
        }

        return error
    }
}