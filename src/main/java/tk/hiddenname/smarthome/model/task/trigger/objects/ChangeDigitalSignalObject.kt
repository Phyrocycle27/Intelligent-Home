package tk.hiddenname.smarthome.model.task.trigger.objects

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
@Table(name = "trigger_change_digital_signal")
@JsonNaming(SnakeCaseStrategy::class)
class ChangeDigitalSignalObject(
        @field:Min(1)
        @field:NotNull(message = "sensor id should be specified")
        @Column(name = "sensor_id", nullable = false, updatable = false)
        val sensorId: Long? = null,

        @field:Min(0)
        @field:Max(86400)
        @Column(nullable = false)
        val delay: Int = 0,

        @field:NotNull(message = "target digital state should be specified")
        @Column(name = "target_state", nullable = false)
        val targetState: Boolean? = null
) : TriggerObject() {

    override fun toString(): String {
        return "ChangeDigitalSignalObject(sensorId=$sensorId, delay=$delay, targetState=$targetState)"
    }
}