package tk.hiddenname.smarthome.model.hardware

import tk.hiddenname.smarthome.model.AbstractJpaPersistableWithTimestamps
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@MappedSuperclass
abstract class Hardware(
        @field:Size(min = 3, max = 25)
        @field:NotBlank(message = "name shouldn't be empty or null ")
        @Column(nullable = false, length = 25)
        var name: String? = null,

        @field:Size(max = 50)
        @Column(nullable = false, length = 50)
        var description: String = "",

        @Column(nullable = false)
        var signalInversion: Boolean = false,

        @field:Min(0)
        @Column(nullable = false)
        var areaId: Long = 0L,

        @field:Valid
        @field:NotNull(message = "gpio object shouldn't be null")
        @AttributeOverrides(value = [
            AttributeOverride(name = "type", column = Column(nullable = false, updatable = true)),
            AttributeOverride(name = "gpioPin", column = Column(nullable = false, updatable = false)),
            AttributeOverride(name = "mode", column = Column(nullable = false, updatable = false))
        ])
        @Embedded val gpio: GPIO? = null
) : AbstractJpaPersistableWithTimestamps() {

    override fun toString(): String {
        return "Hardware(id=$id, areaId=$areaId, name=$name, description=$description, " +
                "creationTimestamp=$creationTimestamp, updateTimestamp=$updateTimestamp, " +
                "signalInversion=$signalInversion, gpioPin=${gpio?.gpioPin}, " +
                "signalType=${gpio?.signalType}, mode=${gpio?.pinMode})"
    }
}