package tk.hiddenname.smarthome.entity.task.processing.objects;

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
@EqualsAndHashCode(callSuper = true)
@Table(name = "processing_device_set_signal")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor
public abstract class DeviceSetSignalObject extends ProcessingObject {

    public DeviceSetSignalObject(ProcessingAction action, Integer deviceId, SignalType signalType, String targetSignal) {
        super(action);
        this.deviceId = deviceId;
        this.signalType = signalType;
        this.targetSignal = targetSignal;
    }

    @Column(name = "device_id", nullable = false)
    private Integer deviceId;

    @Column(nullable = false, length = 7)
    @Enumerated(EnumType.STRING)
    private SignalType signalType;

    @Column(name = "target_signal", nullable = false, length = 5)
    private String targetSignal;

    @Column(nullable = false)
    private Integer delay;
}
