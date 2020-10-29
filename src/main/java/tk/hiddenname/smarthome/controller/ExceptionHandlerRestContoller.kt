package tk.hiddenname.smarthome.controller


import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import tk.hiddenname.smarthome.model.ApiError
import tk.hiddenname.smarthome.model.CustomFieldError
import java.time.LocalDateTime

@RestControllerAdvice
class ExceptionHandlerRestContoller {

    private val log: Logger = LoggerFactory.getLogger(ExceptionHandlerRestContoller::class.java)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun catchMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ApiError {
        return processFieldErrors(ex.bindingResult.fieldErrors)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDefinitionException::class)
    fun catchInvalidSignalTypeException(ex: InvalidDefinitionException): ApiError {
        return ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.cause?.message ?: "")
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