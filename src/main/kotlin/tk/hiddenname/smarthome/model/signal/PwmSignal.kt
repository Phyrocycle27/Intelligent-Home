package tk.hiddenname.smarthome.model.signal

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@JsonNaming(SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["hardwareId", "pwmSignal"])
class PwmSignal(
    @field:Max(1024)
    @field:Min(0)
    @field:NotNull(message = "pwm signal should be specified")
    val pwmSignal: Int? = null
) : Signal() {

    constructor (hardwareId: Long, pwmSignal: Int?) : this(pwmSignal) {
        super.hardwareId = hardwareId
    }
}