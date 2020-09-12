package tk.hiddenname.smarthome.service.hardware.impl.digital.input;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SensorNotFoundException;
import tk.hiddenname.smarthome.service.task.impl.listener.impl.ChangeDigitalSignalListener;
import tk.hiddenname.smarthome.utils.gpio.GpioManager;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class DigitalSensorServiceImpl implements DigitalSensorService {

    private static final Logger log = LoggerFactory.getLogger(DigitalSensorServiceImpl.class);

    private final Map<Integer, GpioPinDigitalInput> map = new HashMap<>();
    private final GpioManager gpioManager;

    @Override
    public void delete(Integer id) {
        gpioManager.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Integer id, Integer gpio, boolean reverse)
            throws GPIOBusyException, PinSignalSupportException {

        map.put(id, gpioManager.createDigitalInput(gpio));
    }

    @Override
    public void update(Integer id, boolean reverse) {
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
    public GpioPinListenerDigital addListener(ChangeDigitalSignalListener listener, Integer sensorId,
                                              boolean targetSignal, boolean reverse) {

        GpioPinDigitalInput pin = map.getOrDefault(sensorId, null);

        if (pin != null) {
            GpioPinListenerDigital pinListener = event ->
                    listener.trigger((event.getState().isHigh() ^ reverse) == targetSignal);
            pin.addListener(pinListener);
            return pinListener;
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
