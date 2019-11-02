package tk.hiddenname.smarthome.service.digital;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.exception.OutputAlreadyExistException;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.service.OutputService;
import tk.hiddenname.smarthome.utils.gpio.GPIO;
import tk.hiddenname.smarthome.utils.gpio.OutputController;

import java.util.HashMap;
import java.util.Map;

@Service
public class DigitalOutputServiceImpl implements OutputService, DigitalOutputService {

    private static Map<Integer, GpioPinDigitalOutput> map;
    private final OutputController controller;

    @Autowired
    public DigitalOutputServiceImpl(OutputController controller) {
        this.controller = controller;
        map = new HashMap<>();
    }

    @Override
    public void delete(Integer id) {
        controller.setState(map.get(id), false);
        GPIO.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Integer id, Integer gpio, String name, Boolean reverse)
            throws OutputAlreadyExistException, PinSignalSupportException {
        map.put(id, GPIO.createDigitalPin(gpio, name, reverse));
    }

    @Override
    public void update(Integer id, String name, Boolean reverse) {
        map.get(id).setName(name);
        setState(id, reverse, getState(id).getDigitalState());
    }

    @Override
    public DigitalState getState(Integer id) {
        GpioPinDigitalOutput pin = map.getOrDefault(id, null);

        if (pin == null) throw new OutputNotFoundException(id);
        return new DigitalState(id, pin.getState().isHigh());
    }

    @Override
    public DigitalState getState(Integer id, Boolean reverse) {
        GpioPinDigitalOutput pin = map.getOrDefault(id, null);

        if (pin == null) throw new OutputNotFoundException(id);
        return new DigitalState(id, reverse ^ pin.getState().isHigh());
    }

    @Override
    public DigitalState setState(Integer id, Boolean reverse, Boolean newState) {
        GpioPinDigitalOutput pin = map.getOrDefault(id, null);

        if (pin == null) throw new OutputNotFoundException(id);
        return new DigitalState(id, reverse ^ controller.setState(pin, reverse ^ newState));
    }

    public Map<Integer, GpioPinDigitalOutput> getMap() {
        return map;
    }
}