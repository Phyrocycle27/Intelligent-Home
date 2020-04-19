package tk.hiddenname.smarthome.service.pwm;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;
import tk.hiddenname.smarthome.exception.OutputAlreadyExistException;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.service.OutputService;
import tk.hiddenname.smarthome.utils.gpio.GPIO;
import tk.hiddenname.smarthome.utils.gpio.OutputSignalController;

import java.util.HashMap;
import java.util.Map;

@Service
public class PwmOutputServiceImpl implements OutputService, PwmOutputService {

    private static Map<Integer, GpioPinPwmOutput> map;
    private final OutputSignalController controller;

    @Autowired
    public PwmOutputServiceImpl(OutputSignalController controller) {
        this.controller = controller;
        map = new HashMap<>();
    }

    @Override
    public void delete(Integer id) {
        controller.setSignal(map.get(id), 0);
        GPIO.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Integer id, Integer gpio, String name, Boolean reverse)
            throws OutputAlreadyExistException, PinSignalSupportException {
        map.put(id, GPIO.createPwmPin(gpio, name, reverse));
    }

    @Override
    public void update(Integer id, String name) {
        map.get(id).setName(name);
    }

    @Override
    public void update(Integer id, String name, Boolean reverse) {
        map.get(id).setName(name);
        setSignal(id, reverse, getSignal(id).getPwmSignal());
    }

    @Override
    public PwmSignal getSignal(Integer id) {
        GpioPinPwmOutput pin = map.getOrDefault(id, null);

        if (pin == null) throw new OutputNotFoundException(id);
        return new PwmSignal(id, pin.getPwm());
    }

    @Override
    public PwmSignal getSignal(Integer id, Boolean reverse) {
        if (!map.containsKey(id)) {
            throw new OutputNotFoundException(id);
        }
        int signal = getSignal(id).getPwmSignal();
        return new PwmSignal(id, reverse ? GPIO.getPwmRange() - signal : signal);
    }

    @Override
    public PwmSignal setSignal(Integer id, Boolean reverse, Integer newSignal) {
        GpioPinPwmOutput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new OutputNotFoundException(id);
        }

        int currSignal = controller.setSignal(pin, reverse ? GPIO.getPwmRange() - newSignal : newSignal);
        return new PwmSignal(id, reverse ? GPIO.getPwmRange() - currSignal : currSignal);
    }
}
