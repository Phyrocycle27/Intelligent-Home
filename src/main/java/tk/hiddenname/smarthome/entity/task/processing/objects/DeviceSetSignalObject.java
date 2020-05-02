package tk.hiddenname.smarthome.entity.task.processing.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.signal.SignalType;

import javax.persistence.*;

@Data
@Entity
@Table(name = "processing_device_set_signal")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DeviceSetSignalObject extends ProcessingObject {

    @Column(name = "device_id", nullable = false)
    private Integer deviceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SignalType type;

    @Column(name = "target_signal", nullable = false)
    private String targetSignal;
}
