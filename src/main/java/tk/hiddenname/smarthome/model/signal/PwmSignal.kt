package tk.hiddenname.smarthome.model.signal

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
class PwmSignal(id: Long, val pwmSignal: Int) : Signal(id)