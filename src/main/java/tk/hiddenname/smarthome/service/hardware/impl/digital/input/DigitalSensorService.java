package tk.hiddenname.smarthome.service.hardware.impl.digital.input;

import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import tk.hiddenname.smarthome.model.signal.DigitalState;
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService;
import tk.hiddenname.smarthome.service.task.impl.listener.Listener;

public interface DigitalSensorService extends GPIOService {

    DigitalState getState(Long id, boolean reverse);

    GpioPinListenerDigital addListener(Listener listener, Long sensorId, boolean targetSignal,
                                       boolean reverse);

    void removeListener(GpioPinListenerDigital listener, Long sensorId);
}
