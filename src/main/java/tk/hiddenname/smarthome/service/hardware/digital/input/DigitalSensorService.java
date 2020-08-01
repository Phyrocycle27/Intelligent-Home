package tk.hiddenname.smarthome.service.hardware.digital.input;

import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.service.task.listener.EventListener;

public interface DigitalSensorService {

    DigitalState getState(Integer id, Boolean reverse);

    void addListener(Integer sensorId, Integer triggerId, EventListener listener, boolean targetSignal);
}
