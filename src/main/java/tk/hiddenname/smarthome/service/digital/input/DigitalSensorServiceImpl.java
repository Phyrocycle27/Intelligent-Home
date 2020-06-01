package tk.hiddenname.smarthome.service.digital.input;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SensorNotFoundException;
import tk.hiddenname.smarthome.service.EventListener;
import tk.hiddenname.smarthome.service.GPIOService;
import tk.hiddenname.smarthome.service.digital.output.DigitalDeviceServiceImpl;
import tk.hiddenname.smarthome.utils.gpio.GPIOManager;

import java.util.HashMap;
import java.util.Map;

@Service
public class DigitalSensorServiceImpl implements GPIOService, DigitalSensorService {

    private static final Logger log = LoggerFactory.getLogger(DigitalDeviceServiceImpl.class);
    private final Map<Integer, GpioPinDigitalInput> map = new HashMap<>();

    @Override
    public void delete(Integer id) {
        GPIOManager.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Integer id, Integer gpio, Boolean reverse)
            throws GPIOBusyException, PinSignalSupportException {

        map.put(id, GPIOManager.createDigitalInput(gpio));
    }

    @Override
    public void update(Integer id, Boolean reverse) {
    }

    @Override
    public DigitalState getState(Integer id, Boolean reverse) {
        GpioPinDigitalInput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new SensorNotFoundException(id);
        }
        return new DigitalState(id, reverse ^ pin.isHigh());
    }

    @Override
    public void addListener(Integer sensorId, Integer triggerId, EventListener listener, boolean targetSignal) {
        GpioPinDigitalInput pin = map.getOrDefault(sensorId, null);

        if (pin == null) {
            throw new SensorNotFoundException(sensorId);
        }

        pin.addListener((GpioPinListenerDigital) event -> {
                    log.info("Sensor with id " + sensorId + "triggered");
                    listener.update(triggerId, event.getState().isHigh() == targetSignal);
                }
        );
        log.info("* Listener added");
    }
}
