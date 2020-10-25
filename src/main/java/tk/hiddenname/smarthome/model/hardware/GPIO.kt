package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import tk.hiddenname.smarthome.model.signal.SignalType
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class GPIO(
        var gpioPin: Int,

        @Enumerated(EnumType.STRING)
        var type: SignalType,

        @Enumerated(EnumType.STRING)
        var mode: GPIOMode
) {
    constructor() : this(0, SignalType.DIGITAL, GPIOMode.OUTPUT)
}