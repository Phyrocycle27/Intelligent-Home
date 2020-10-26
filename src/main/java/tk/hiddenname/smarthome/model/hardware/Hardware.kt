package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@MappedSuperclass
abstract class Hardware(
        @Column(nullable = false, length = 25)
        val name: @Size(min = 3, max = 25) String = "",

        @Column(nullable = false, length = 50)
        val description: @Size(min = 3, max = 50) String = "",

        @Column(nullable = false)
        val signalInversion: Boolean = false,

        @Column(nullable = false)
        val areaId: Long = 0,

        @Column(nullable = false, updatable = false, name = "creation_timestamp")
        @get:JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "yyyy-MM-dd HH:mm:ss")
        @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
        var creationTimestamp: LocalDateTime? = null,

        @Column(nullable = false, updatable = true, name = "update_timestamp")
        @get:JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "yyyy-MM-dd HH:mm:ss")
        @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
        var updateTimestamp: LocalDateTime? = null,

        @Embedded
        @AttributeOverrides(value = [
            AttributeOverride(name = "type", column = Column(nullable = false, updatable = false)),
            AttributeOverride(name = "gpioPin", column = Column(nullable = false, updatable = false)),
            AttributeOverride(name = "mode", column = Column(nullable = false, updatable = false))
        ])
        val gpio: GPIO? = null
) : AbstractJpaPersistable() {

    override fun toString(): String {
        return "Hardware(id=$id, areaId=$areaId, name=$name, description=$description, " +
                "creationTimestamp=$creationTimestamp, updateTimestamp=$updateTimestamp" +
                "signalInversion=$signalInversion, gpioPin=${gpio?.gpioPin}, " +
                "signalType=${gpio?.signalType}, mode=${gpio?.pinMode})"
    }
}