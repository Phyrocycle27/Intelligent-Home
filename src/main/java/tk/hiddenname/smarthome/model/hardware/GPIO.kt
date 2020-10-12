package tk.hiddenname.smarthome.model.hardware

import org.jetbrains.annotations.NotNull
import tk.hiddenname.smarthome.model.signal.SignalType
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class GPIO(@NotNull private var gpioPin: Int = 0,

           @NotNull
           @Enumerated(EnumType.STRING)
           private var type: SignalType? = null,

           @NotNull
           @Enumerated(EnumType.STRING)
           private var mode: GPIOMode? = null
)