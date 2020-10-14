package tk.hiddenname.smarthome.model.signal

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
class DigitalState(id: Long, var isDigitalState: Boolean) : Signal(id)