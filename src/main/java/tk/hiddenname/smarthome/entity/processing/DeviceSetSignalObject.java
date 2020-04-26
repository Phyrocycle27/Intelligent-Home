package tk.hiddenname.smarthome.entity.processing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.hiddenname.smarthome.entity.signal.SignalObject;

@Data
@EqualsAndHashCode(of = {"deviceId"}, callSuper = false)
@AllArgsConstructor
public class DeviceSetSignalObject extends ProcessingObject {

    private Integer deviceId;
    private SignalObject targetSignal;
}
