package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import tk.hiddenname.smarthome.model.signal.SignalType
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Embeddable
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class GPIO(
    @field:Max(30)
    @field:Min(1)
    @field:NotNull(message = "gpio pin should be specified")
    val gpioPin: Int? = null,

    @Enumerated(EnumType.STRING)
    @field:NotNull(message = "gpio signal type shouldn't be null")
    var signalType: SignalType? = null,

    @Enumerated(EnumType.STRING)
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var pinMode: GpioMode = GpioMode.NOT_SPECIFIED
)