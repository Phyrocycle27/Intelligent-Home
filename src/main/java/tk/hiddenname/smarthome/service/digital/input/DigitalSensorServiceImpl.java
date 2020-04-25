package tk.hiddenname.smarthome.service.digital.input;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.service.GPIOService;
import tk.hiddenname.smarthome.utils.gpio.GPIOManager;

import java.util.HashMap;
import java.util.Map;

@Service
public class DigitalSensorServiceImpl implements GPIOService, DigitalSensorService {

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
            throw new DeviceNotFoundException(id);
        }
        return new DigitalState(id, reverse ^ pin.isHigh());
    }
}
