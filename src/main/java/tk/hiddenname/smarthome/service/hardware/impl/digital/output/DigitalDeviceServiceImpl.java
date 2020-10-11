package tk.hiddenname.smarthome.service.hardware.impl.digital.output;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.model.signal.DigitalState;
import tk.hiddenname.smarthome.utils.gpio.GpioManager;
import tk.hiddenname.smarthome.utils.gpio.OutputSignalController;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class DigitalDeviceServiceImpl implements DigitalDeviceService {

    private final Map<Long, GpioPinDigitalOutput> map = new HashMap<>();

    private final OutputSignalController controller;
    private final GpioManager gpioManager;

    @Override
    public void delete(Long id) {
        controller.setState(map.get(id), false);
        gpioManager.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Long id, int gpioPin, boolean reverse)
            throws GPIOBusyException, PinSignalSupportException {

        map.put(id, gpioManager.createDigitalOutput(gpioPin, reverse));
    }

    @Override
    public void update(Long id, boolean reverse) {
        setState(id, reverse, getState(id, reverse).isDigitalState());
    }

    @Override
    public DigitalState getState(Long id, Boolean reverse) {
        return new DigitalState(id, reverse ^ getPin(id).isHigh());
    }

    @Override
    public DigitalState setState(Long id, Boolean reverse, Boolean newState) {
        return new DigitalState(id, reverse ^ controller.setState(getPin(id), reverse ^ newState));
    }

    private GpioPinDigitalOutput getPin(Long id) {
        GpioPinDigitalOutput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new DeviceNotFoundException(id);
        }

        return pin;
    }
}