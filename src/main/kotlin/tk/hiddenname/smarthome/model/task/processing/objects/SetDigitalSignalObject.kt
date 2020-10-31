package tk.hiddenname.smarthome.model.task.processing.objects

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
@Table(name = "processing_set_digital_signal")
@JsonNaming(SnakeCaseStrategy::class)
class SetDigitalSignalObject(
        @field:Min(1)
        @field:NotNull(message = "device id should be specified")
        @Column(name = "device_id", nullable = false, updatable = false)
        val deviceId: Long? = null,

        @field:Min(0)
        @field:Max(86400)
        @Column(nullable = false)
        val delay: Int = 0,

        @field:NotNull(message = "target digital state should be specified")
        @Column(name = "target_state", nullable = false)
        val targetState: Boolean? = null
) : ProcessingObject() {
    override fun toString(): String {
        return "SetDigitalSignalObject(deviceId=$deviceId, delay=$delay, targetState=$targetState)"
    }
}