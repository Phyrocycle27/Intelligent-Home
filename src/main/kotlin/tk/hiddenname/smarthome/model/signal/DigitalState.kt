package tk.hiddenname.smarthome.model.signal

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.validation.constraints.NotNull

@JsonNaming(SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["hardwareId", "digitalState"])
class DigitalState(
        @field:NotNull(message = "digital state should be specified")
        val digitalState: Boolean?
) : Signal() {

    constructor (hardwareId: Long, digitalState: Boolean?) : this(digitalState) {
        super.hardwareId = hardwareId
    }
}