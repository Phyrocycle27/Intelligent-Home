package tk.hiddenname.smarthome.entity.task.trigger.objects;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.entity.task.trigger.TriggerAction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "trigger_sensor_change_signal")
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor
public class SensorChangeSignalObject extends TriggerObject {

    public SensorChangeSignalObject(TriggerAction action, Integer sensorId, SignalType signalType, String triggerSignal) {
        super(action);
        this.sensorId = sensorId;
        this.signalType = signalType;
        this.triggerSignal = triggerSignal;
    }

    @Column(name = "sensor_id", nullable = false)
    private Integer sensorId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SignalType signalType;

    @Column(name = "trigger_signal", nullable = false)
    private String triggerSignal;
}
