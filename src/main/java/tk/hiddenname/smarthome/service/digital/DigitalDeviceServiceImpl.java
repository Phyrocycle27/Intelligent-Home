package tk.hiddenname.smarthome.service.digital;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.exception.DeviceAlreadyExistException;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.service.GPIOService;
import tk.hiddenname.smarthome.utils.gpio.GPIOManager;
import tk.hiddenname.smarthome.utils.gpio.OutputSignalController;

import java.util.HashMap;
import java.util.Map;

@Service
public class DigitalDeviceServiceImpl implements GPIOService, DigitalDeviceService {

    private static Map<Integer, GpioPinDigitalOutput> map;

    private final OutputSignalController controller;

    @Autowired
    public DigitalDeviceServiceImpl(OutputSignalController controller) {
        this.controller = controller;
        map = new HashMap<>();
    }

    @Override
    public void delete(Integer id) {
        controller.setState(map.get(id), false);
        GPIOManager.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Integer id, Integer gpio, Boolean reverse)
            throws DeviceAlreadyExistException, PinSignalSupportException {

        map.put(id, GPIOManager.createDigitalOutput(gpio, reverse));
    }

    @Override
    public void update(Integer id, Boolean reverse) {
        setState(id, reverse, getState(id, reverse).getDigitalState());
    }

    @Override
    public DigitalState getState(Integer id, Boolean reverse) {
        GpioPinDigitalOutput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new DeviceNotFoundException(id);
        }
        return new DigitalState(id, reverse ^ pin.getState().isHigh());
    }

    @Override
    public DigitalState setState(Integer id, Boolean reverse, Boolean newState) {
        GpioPinDigitalOutput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new DeviceNotFoundException(id);
        }
        return new DigitalState(id, reverse ^ controller.setState(pin, reverse ^ newState));
    }
}