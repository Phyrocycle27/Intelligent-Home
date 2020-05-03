package tk.hiddenname.smarthome.service.digital.input;

import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.service.EventListener;

public interface DigitalSensorService {

    DigitalState getState(Integer id, Boolean reverse);

    void addListener(Integer sensorId, Integer triggerId, EventListener listener, boolean targetSignal);
}
