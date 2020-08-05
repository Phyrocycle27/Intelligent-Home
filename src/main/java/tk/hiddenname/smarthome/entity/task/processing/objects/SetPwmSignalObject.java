package tk.hiddenname.smarthome.entity.task.processing.objects;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.entity.task.processing.ProcessingAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "processing-pwm_set_signal")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SetPwmSignalObject extends SetSignalObject {

    @Column(nullable = false)
    private int targetSignal;

    public SetPwmSignalObject(ProcessingAction action, Integer deviceId, SignalType signalType, int delay,
                              int targetSignal) {
        super(action, deviceId, signalType, delay);
        this.targetSignal = targetSignal;
    }
}
