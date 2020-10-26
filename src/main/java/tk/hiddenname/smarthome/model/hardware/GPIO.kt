package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import tk.hiddenname.smarthome.model.signal.SignalType
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class GPIO(
        var gpioPin: Int = 0,

        @Enumerated(EnumType.STRING)
        var signalType: SignalType = SignalType.NOT_SPECIFIED,

        @Enumerated(EnumType.STRING)
        @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
        var pinMode: GPIOMode = GPIOMode.NOT_SPECIFIED
)