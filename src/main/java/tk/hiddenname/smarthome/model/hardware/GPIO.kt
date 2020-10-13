package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.jetbrains.annotations.NotNull
import tk.hiddenname.smarthome.model.signal.SignalType
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class GPIO(@NotNull var gpioPin: Int = 0,

           @NotNull
           @Enumerated(EnumType.STRING)
           var type: SignalType? = null,

           @NotNull
           @Enumerated(EnumType.STRING)
           var mode: GPIOMode? = null
)