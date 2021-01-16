package tk.hiddenname.smarthome.model.signal

import javax.persistence.MappedSuperclass
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@MappedSuperclass
abstract class Signal(
    @field:NotNull(message = "hardware id should be specified")
    @field:Min(value = 0, message = "Hardware Id should be equal or rather than 0")
    var hardwareId: Long? = null
)