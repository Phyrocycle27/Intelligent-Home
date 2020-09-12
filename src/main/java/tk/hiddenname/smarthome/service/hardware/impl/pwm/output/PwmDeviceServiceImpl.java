package tk.hiddenname.smarthome.service.hardware.impl.pwm.output;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.utils.gpio.GpioManager;
import tk.hiddenname.smarthome.utils.gpio.OutputSignalController;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class PwmDeviceServiceImpl implements PwmDeviceService {

    private static final Map<Integer, GpioPinPwmOutput> map = new HashMap<>();
    private final OutputSignalController controller;
    private final GpioManager gpioManager;

    @Override
    public void delete(Integer id) {
        controller.setSignal(map.get(id), 0);
        gpioManager.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Integer id, Integer gpio, boolean reverse)
            throws GPIOBusyException, PinSignalSupportException {
        map.put(id, gpioManager.createPwmOutput(gpio, reverse));
    }

    @Override
    public void update(Integer id, boolean reverse) {
        setSignal(id, reverse, getSignal(id, reverse).getPwmSignal());
    }

    @Override
    public PwmSignal getSignal(Integer id, Boolean reverse) {
        GpioPinPwmOutput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new DeviceNotFoundException(id);
        }
        int signal = pin.getPwm();
        return new PwmSignal(id, reverse ? gpioManager.getPwmRange() - signal : signal);
    }

    @Override
    public PwmSignal setSignal(Integer id, Boolean reverse, Integer newSignal) {
        GpioPinPwmOutput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new DeviceNotFoundException(id);
        }

        int currSignal = controller.setSignal(pin, reverse ? gpioManager.getPwmRange() - newSignal : newSignal);
        return new PwmSignal(id, reverse ? gpioManager.getPwmRange() - currSignal : currSignal);
    }
}
