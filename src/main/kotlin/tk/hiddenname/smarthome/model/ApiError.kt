package tk.hiddenname.smarthome.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime

@Suppress("unused")
@JsonNaming(SnakeCaseStrategy::class)
class ApiError(
        @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        val timestamp: LocalDateTime,
        val status: Int,
        val error: String,
) {
    private val fieldErrors: MutableSet<CustomFieldError> = HashSet()

    fun addFieldError(fieldError: CustomFieldError) = fieldErrors.add(fieldError)
}