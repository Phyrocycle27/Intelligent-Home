package tk.hiddenname.smarthome.service.pwm;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;
import tk.hiddenname.smarthome.exception.DeviceAlreadyExistException;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.service.OutputService;
import tk.hiddenname.smarthome.utils.gpio.GPIOManager;
import tk.hiddenname.smarthome.utils.gpio.OutputSignalController;

import java.util.HashMap;
import java.util.Map;

@Service
public class PwmDeviceServiceImpl implements OutputService, PwmDeviceService {

    private static Map<Integer, GpioPinPwmOutput> map;
    private final OutputSignalController controller;

    @Autowired
    public PwmDeviceServiceImpl(OutputSignalController controller) {
        this.controller = controller;
        map = new HashMap<>();
    }

    @Override
    public void delete(Integer id) {
        controller.setSignal(map.get(id), 0);
        GPIOManager.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Integer id, Integer gpio, Boolean reverse)
            throws DeviceAlreadyExistException, PinSignalSupportException {
        map.put(id, GPIOManager.createPwmOutput(gpio, reverse));
    }

    @Override
    public void update(Integer id, Boolean reverse) {
        setSignal(id, reverse, getSignal(id, reverse).getPwmSignal());
    }

    @Override
    public PwmSignal getSignal(Integer id, Boolean reverse) {
        GpioPinPwmOutput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new DeviceNotFoundException(id);
        }
        int signal = pin.getPwm();
        return new PwmSignal(id, reverse ? GPIOManager.getPwmRange() - signal : signal);
    }

    @Override
    public PwmSignal setSignal(Integer id, Boolean reverse, Integer newSignal) {
        GpioPinPwmOutput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new DeviceNotFoundException(id);
        }

        int currSignal = controller.setSignal(pin, reverse ? GPIOManager.getPwmRange() - newSignal : newSignal);
        return new PwmSignal(id, reverse ? GPIOManager.getPwmRange() - currSignal : currSignal);
    }
}
