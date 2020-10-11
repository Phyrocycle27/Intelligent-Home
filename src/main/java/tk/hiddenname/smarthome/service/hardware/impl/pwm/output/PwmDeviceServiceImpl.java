package tk.hiddenname.smarthome.service.hardware.impl.pwm.output;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.model.signal.PwmSignal;
import tk.hiddenname.smarthome.utils.gpio.GpioManager;
import tk.hiddenname.smarthome.utils.gpio.OutputSignalController;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class PwmDeviceServiceImpl implements PwmDeviceService {

    private static final Map<Long, GpioPinPwmOutput> map = new HashMap<>();
    private final OutputSignalController controller;
    private final GpioManager gpioManager;

    @Override
    public void delete(Long id) {
        controller.setSignal(map.get(id), 0);
        gpioManager.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Long id, int gpioPin, boolean reverse)
            throws GPIOBusyException, PinSignalSupportException {
        map.put(id, gpioManager.createPwmOutput(gpioPin, reverse));
    }

    @Override
    public void update(Long id, boolean reverse) {
        setSignal(id, reverse, getSignal(id, reverse).getPwmSignal());
    }

    @Override
    public PwmSignal getSignal(Long id, boolean reverse) {
        int signal = getPin(id).getPwm();
        return new PwmSignal(id, reverse ? gpioManager.getPwmRange() - signal : signal);
    }

    @Override
    public PwmSignal setSignal(Long id, boolean reverse, int newSignal) {
        int currSignal = controller.setSignal(getPin(id), reverse ? gpioManager.getPwmRange() - newSignal : newSignal);
        return new PwmSignal(id, reverse ? gpioManager.getPwmRange() - currSignal : currSignal);
    }

    private GpioPinPwmOutput getPin(Long id) {
        GpioPinPwmOutput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new DeviceNotFoundException(id);
        }

        return pin;
    }
}
