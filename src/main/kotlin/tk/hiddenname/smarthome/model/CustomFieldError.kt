package tk.hiddenname.smarthome.model

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@Suppress("unused")
@JsonNaming(SnakeCaseStrategy::class)
class CustomFieldError(
        val message: String,
        val objectName: String,
        val fieldName: String
) {
    override fun toString(): String {
        return "CustomFieldError(message='$message', objectName='$objectName', fieldName='$fieldName')"
    }
}