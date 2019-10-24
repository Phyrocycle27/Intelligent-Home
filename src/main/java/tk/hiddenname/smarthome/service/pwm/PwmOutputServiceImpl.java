package tk.hiddenname.smarthome.service.pwm;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.service.OutputService;
import tk.hiddenname.smarthome.utils.gpio.GPIO;
import tk.hiddenname.smarthome.utils.gpio.OutputController;

import java.util.HashMap;
import java.util.Map;

@Service
public class PwmOutputServiceImpl implements OutputService, PwmOutputService {

    private static Map<Integer, GpioPinPwmOutput> map;
    private final OutputController controller;

    @Autowired
    public PwmOutputServiceImpl(OutputController controller) {
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
    public void save(Integer id, Integer gpio, String name, Boolean reverse) {
        map.put(id, GPIO.createPwmPin(gpio, name, reverse));
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
        GpioPinPwmOutput pin = map.getOrDefault(id, null);

        if (pin == null) throw new OutputNotFoundException(id);
        return new PwmSignal(id, reverse ? GPIO.getPwmRange() - pin.getPwm() : pin.getPwm());
    }

    @Override
    public PwmSignal setSignal(Integer id, Boolean reverse, Integer newSignal) {
        GpioPinPwmOutput pin = map.getOrDefault(id, null);

        if (pin == null) throw new OutputNotFoundException(id);

        Integer currSignal = controller.setSignal(map.get(id), reverse ? GPIO.getPwmRange() - newSignal : newSignal);
        return new PwmSignal(id, reverse ? GPIO.getPwmRange() - currSignal : currSignal);
    }

    public Map<Integer, GpioPinPwmOutput> getMap() {
        return map;
    }
}
