package tk.hiddenname.smarthome.service.hardware.impl.digital.input;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SensorNotFoundException;
import tk.hiddenname.smarthome.model.signal.DigitalState;
import tk.hiddenname.smarthome.service.task.impl.listener.Listener;
import tk.hiddenname.smarthome.utils.gpio.GpioManager;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class DigitalSensorServiceImpl implements DigitalSensorService {

    private final Map<Long, GpioPinDigitalInput> map = new HashMap<>();
    private final GpioManager gpioManager;

    @Override
    public void delete(Long id) {
        gpioManager.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Long id, int gpioPin, boolean reverse)
            throws GPIOBusyException, PinSignalSupportException {

        map.put(id, gpioManager.createDigitalInput(gpioPin));
    }

    @Override
    public void update(Long id, boolean reverse) {
    }

    @Override
    public DigitalState getState(Long id, boolean reverse) {
        GpioPinDigitalInput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new SensorNotFoundException(id);
        }
        return new DigitalState(id, reverse ^ pin.isHigh());
    }


    @Override
    public GpioPinListenerDigital addListener(Listener listener, Long sensorId,
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
    public void removeListener(GpioPinListenerDigital listener, Long sensorId) {
        GpioPinDigitalInput pin = map.getOrDefault(sensorId, null);

        if (pin != null) {
            pin.removeListener(listener);
        } else {
            throw new SensorNotFoundException(sensorId);
        }
    }
}
