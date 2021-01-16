package tk.hiddenname.smarthome.model.error

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

@Suppress("unused")
open class ApiError(
    @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: LocalDateTime? = null,
    val status: Int = 0,
    val error: String = ""
)