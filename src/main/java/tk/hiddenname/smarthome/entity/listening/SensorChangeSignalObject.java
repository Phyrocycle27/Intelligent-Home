package tk.hiddenname.smarthome.entity.listening;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.hiddenname.smarthome.entity.signal.SignalObject;

@Data
@EqualsAndHashCode(of = {"sensorId"}, callSuper = false)
@AllArgsConstructor
public class SensorChangeSignalObject extends ListeningObject {

    private Integer sensorId;
    private SignalObject listeningSignal;
}
