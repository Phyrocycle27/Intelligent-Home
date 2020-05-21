package tk.hiddenname.smarthome.entity.task.processing.objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.entity.task.processing.ProcessingAction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "processing_device_set_signal")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DeviceSetSignalObject extends ProcessingObject {

    public DeviceSetSignalObject(ProcessingAction action, Integer deviceId, SignalType signalType, String targetSignal) {
        super(action);
        this.deviceId = deviceId;
        this.signalType = signalType;
        this.targetSignal = targetSignal;
    }

    public static String name = "HELLO";

    @Column(name = "device_id", nullable = false)
    private Integer deviceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SignalType signalType;

    @Column(name = "target_signal", nullable = false)
    private String targetSignal;
}
