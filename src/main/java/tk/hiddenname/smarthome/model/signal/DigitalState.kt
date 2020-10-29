package tk.hiddenname.smarthome.model.signal

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.validation.constraints.NotNull

@JsonNaming(SnakeCaseStrategy::class)
class DigitalState(
        @field:NotNull(message = "digital state should be specified")
        val digitalState: Boolean? = null
) : Signal() {

    constructor (hardwareId: Long, digitalState: Boolean?) : this(digitalState) {
        super.hardwareId = hardwareId
    }
}