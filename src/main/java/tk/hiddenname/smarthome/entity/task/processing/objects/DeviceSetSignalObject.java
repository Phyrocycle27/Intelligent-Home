package tk.hiddenname.smarthome.entity.task.processing.objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.hiddenname.smarthome.entity.signal.SignalType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Processing_DeviceSetSignal")
@EqualsAndHashCode(callSuper = true)
public class DeviceSetSignalObject extends ProcessingObject {

    @Column(name = "device_id", nullable = false)
    private Integer deviceId;

    @Column(nullable = false)
    private SignalType type;

    @Column(name = "target_signal", nullable = false)
    private String targetSignal;
}
