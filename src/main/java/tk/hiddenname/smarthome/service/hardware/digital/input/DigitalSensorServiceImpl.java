package tk.hiddenname.smarthome.service.hardware.digital.input;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SensorNotFoundException;
import tk.hiddenname.smarthome.service.task.listener.impl.ChangeDigitalSignalListener;
import tk.hiddenname.smarthome.utils.gpio.GPIOManager;

import java.util.HashMap;
import java.util.Map;

@Service
public class DigitalSensorServiceImpl implements DigitalSensorService {

    private static final Logger log = LoggerFactory.getLogger(DigitalSensorServiceImpl.class);
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
    public GpioPinListenerDigital addListener(ChangeDigitalSignalListener listener, Integer sensorId, boolean targetSignal, boolean reverse) {
        GpioPinDigitalInput pin = map.getOrDefault(sensorId, null);

        if (pin != null) {
            return event -> {
                log.info("Sensor with id " + sensorId + "triggered");
                listener.update((event.getState().isHigh() ^ reverse) == targetSignal);
            };
        } else {
            throw new SensorNotFoundException(sensorId);
        }
    }

    @Override
    public void removeListener(GpioPinListenerDigital listener, Integer sensorId) {
        GpioPinDigitalInput pin = map.getOrDefault(sensorId, null);

        if (pin != null) {
            pin.removeListener(listener);
        } else {
            throw new SensorNotFoundException(sensorId);
        }
    }
}
