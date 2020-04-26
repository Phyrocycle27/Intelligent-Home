package tk.hiddenname.smarthome.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorChangeSignalObject {

    private Integer sensorId;
    private SignalObject targetSignal;
}
