package tk.hiddenname.smarthome.entity.task.trigger.objects;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "trigger_change_digital_signal")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ChangeDigitalSignalObject extends TriggerObject {

    @NotNull
    @Column(name = "sensor_id", nullable = false)
    private Integer sensorId;

    @Column(nullable = false)
    private int delay = 0;

    @NotNull
    @Column(nullable = false)
    private boolean targetState;
}
