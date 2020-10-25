package tk.hiddenname.smarthome.model.task.trigger.objects

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "trigger_change_digital_signal")
@JsonNaming(SnakeCaseStrategy::class)
data class ChangeDigitalSignalObject(
        @Column(name = "sensor_id", nullable = false, updatable = false)
        val sensorId: Long = 0,

        @Column(nullable = false)
        val delay: Int = 0,

        @Column(name = "target_state", nullable = false)
        val targetState: Boolean = false
) : TriggerObject()