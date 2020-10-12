package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class AvailableGpioPins(private val availableGpioPins: Set<Int>)