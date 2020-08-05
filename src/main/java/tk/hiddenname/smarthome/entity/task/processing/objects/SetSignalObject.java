package tk.hiddenname.smarthome.entity.task.processing.objects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.entity.task.processing.ProcessingAction;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "processing-set_signal")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "signalType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SetDigitalSignalObject.class, name = "DIGITAL"),
        @JsonSubTypes.Type(value = SetPwmSignalObject.class, name = "PWM")
})
public abstract class SetSignalObject extends ProcessingObject {

    @Column(name = "device_id", nullable = false)
    private Integer deviceId;

    @Column(name = "signal_type", nullable = false, length = 7)
    @Enumerated(EnumType.STRING)
    private SignalType signalType;

    @Column(nullable = false)
    private int delay;

    public SetSignalObject(ProcessingAction action, Integer deviceId, SignalType signalType, int delay) {
        super(action);
        this.deviceId = deviceId;
        this.signalType = signalType;
        this.delay = delay;
    }
}
