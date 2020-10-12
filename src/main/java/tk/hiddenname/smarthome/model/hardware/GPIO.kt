package tk.hiddenname.smarthome.model.hardware

import lombok.Data
import tk.hiddenname.smarthome.model.signal.SignalType
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull

@Data
@Embeddable
class GPIO(private val gpioPin: Int = 0,
           @Enumerated(EnumType.STRING)
           private val type: @NotNull SignalType? = null) {

    @Enumerated(EnumType.STRING)
    private val mode: @NotNull GPIOMode? = null
}