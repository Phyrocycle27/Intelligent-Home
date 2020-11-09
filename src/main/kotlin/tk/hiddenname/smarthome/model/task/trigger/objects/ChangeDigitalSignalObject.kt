package tk.hiddenname.smarthome.model.task.trigger.objects

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import tk.hiddenname.smarthome.model.task.TaskValidationGroup
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
@Table(name = "trigger_change_digital_signal")
@JsonNaming(SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["id", "action", "sensor_id", "delay", "target_state"])
class ChangeDigitalSignalObject(
        @field:Min(1, groups = [TaskValidationGroup::class])
        @field:NotNull(message = "sensor id should be specified", groups = [TaskValidationGroup::class])
        @Column(name = "sensor_id", nullable = false, updatable = false)
        val sensorId: Long? = null,

        @field:Min(0, groups = [TaskValidationGroup::class])
        @field:Max(86400, groups = [TaskValidationGroup::class])
        @Column(nullable = false)
        val delay: Int = 0,

        @field:NotNull(message = "target digital state should be specified", groups = [TaskValidationGroup::class])
        @Column(name = "target_state", nullable = false)
        val targetState: Boolean? = null
) : TriggerObject() {

    override fun toString(): String {
        return "ChangeDigitalSignalObject(sensorId=$sensorId, delay=$delay, targetState=$targetState)"
    }
}