package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@MappedSuperclass
abstract class Hardware(
        @field:NotBlank(message = "name shouldn't be empty or null ")
        @field:Size(min = 3, max = 25)
        @Column(nullable = false, length = 25)
        var name: String? = null,

        @field:Size(max = 50)
        @Column(nullable = false, length = 50)
        var description: String = "",

        @Column(nullable = false)
        var signalInversion: Boolean = false,

        @Column(nullable = false)
        var areaId: Long = 0L,

        @Column(nullable = false, updatable = false)
        @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
        var creationTimestamp: LocalDateTime? = null,

        @Column(nullable = false, updatable = true)
        @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
        var updateTimestamp: LocalDateTime? = null,

        @field:Valid
        @field:NotNull(message = "gpio object shouldn't be null")
        @Embedded
        @AttributeOverrides(value = [
            AttributeOverride(name = "type", column = Column(nullable = false, updatable = true)),
            AttributeOverride(name = "gpioPin", column = Column(nullable = false, updatable = false)),
            AttributeOverride(name = "mode", column = Column(nullable = false, updatable = false))
        ])
        val gpio: GPIO? = null
) : AbstractJpaPersistable() {

    override fun toString(): String {
        return "Hardware(id=$id, areaId=$areaId, name=$name, description=$description, " +
                "creationTimestamp=$creationTimestamp, updateTimestamp=$updateTimestamp, " +
                "signalInversion=$signalInversion, gpioPin=${gpio?.gpioPin}, " +
                "signalType=${gpio?.signalType}, mode=${gpio?.pinMode})"
    }
}