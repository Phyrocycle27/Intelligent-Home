package tk.hiddenname.smarthome.service.hardware.impl.digital.input;

import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService;
import tk.hiddenname.smarthome.service.task.impl.listener.impl.ChangeDigitalSignalListener;

public interface DigitalSensorService extends GPIOService {

    DigitalState getState(Integer id, Boolean reverse);

    GpioPinListenerDigital addListener(ChangeDigitalSignalListener listener, Integer sensorId, boolean targetSignal, boolean reverse);

    void removeListener(GpioPinListenerDigital listener, Integer sensorId);
}
