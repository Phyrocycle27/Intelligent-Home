package tk.hiddenname.smarthome.entity.task.trigger.objects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.signal.SignalType;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "trigger_change_signal")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "signalType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ChangeDigitalSignalObject.class, name = "DIGITAL"),
        @JsonSubTypes.Type(value = ChangePwmSignalObject.class, name = "PWM")
})
public abstract class ChangeSignalObject extends TriggerObject {

    @Column(name = "sensor_id", nullable = false)
    private Integer sensorId;

    @Column(name = "signal_type", nullable = false, length = 7)
    @Enumerated(EnumType.STRING)
    private SignalType signalType;

    @Column(nullable = false)
    private int delay;
}
