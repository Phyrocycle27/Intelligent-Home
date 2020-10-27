package tk.hiddenname.smarthome.controller


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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(ex: MethodArgumentNotValidException): ApiError {
        return processFieldErrors(ex.bindingResult.fieldErrors)
    }

    private fun processFieldErrors(fieldErrors: List<FieldError>): ApiError {
        val error = ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Validation error")

        fieldErrors.forEach {
            val message = if (it.defaultMessage == null) "" else it.defaultMessage
            val fieldError = CustomFieldError(message, it.objectName, it.field)
            error.addFieldError(fieldError)
        }

        return error
    }
}