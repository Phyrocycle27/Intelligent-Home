package tk.hiddenname.smarthome.model.error

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime

@Suppress("unused")
@JsonNaming(SnakeCaseStrategy::class)
class ValidationError(
        timestamp: LocalDateTime,
        status: Int,
        message: String,
        @Suppress("MemberVisibilityCanBePrivate")
        val fieldErrors: MutableList<CustomFieldError> = mutableListOf()
) : ApiError(timestamp, status, message) {
    fun addFieldError(fieldError: CustomFieldError) = fieldErrors.add(fieldError)
}