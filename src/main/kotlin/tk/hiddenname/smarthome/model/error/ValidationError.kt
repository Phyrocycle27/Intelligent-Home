package tk.hiddenname.smarthome.model.error

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime

@Suppress("unused")
@JsonNaming(SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["timestamp", "status", "error", "fieldErrors"])
class ValidationError(
    timestamp: LocalDateTime,
    status: Int,
    error: String,
    @Suppress("MemberVisibilityCanBePrivate")
    val fieldErrors: MutableList<CustomFieldError> = mutableListOf()
) : ApiError(timestamp, status, error) {
    fun addFieldError(fieldError: CustomFieldError) = fieldErrors.add(fieldError)
}