package tk.hiddenname.smarthome.entity.task.trigger.objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.hiddenname.smarthome.entity.signal.SignalType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Trigger_SensorChangeSignal")
@EqualsAndHashCode(callSuper = true)
public class SensorChangeSignalObject extends TriggerObject {

    @Column(name = "sensor_id", nullable = false)
    private Integer sensorId;

    @Column(nullable = false)
    private SignalType type;

    @Column(name = "trigger_signal", nullable = false)
    private String triggerSignal;
}
