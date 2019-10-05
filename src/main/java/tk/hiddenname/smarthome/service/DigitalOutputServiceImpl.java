package tk.hiddenname.smarthome.service;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.controller.DigitalOutputRestController;
import tk.hiddenname.smarthome.entity.State;
import tk.hiddenname.smarthome.entity.output.DigitalOutput;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.utils.gpio.GPIO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DigitalOutputServiceImpl implements DigitalOutputService {

    private Map<Integer, GpioPinDigitalOutput> map = new HashMap<>();

    @Override
    public void delete(Integer id) {
        map.remove(id);
    }

    @Override
    public void save(DigitalOutput newOutput) {
        map.put(newOutput.getId(), GPIO.convert(newOutput));
    }

    @Override
    public void update(DigitalOutput newOutput, Integer id) {
        map.replace(id, GPIO.convert(newOutput));
    }

    @Override
    public GpioPinDigitalOutput getOne(Integer id) {
        return map.get(id);
    }

    @Override
    public List<GpioPinDigitalOutput> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public State getState(Integer id) {
        GpioPinDigitalOutput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new OutputNotFoundException(DigitalOutputRestController.TYPE, id);
        } else {
            return new State(id, pin.getState().isHigh());
        }
    }

    @Override
    public State setState(Integer id, Boolean newState) {
        GpioPinDigitalOutput pin = map.getOrDefault(id, null);

        if (pin == null) {
            throw new OutputNotFoundException(DigitalOutputRestController.TYPE, id);
        } else {
            pin.setState(newState);
            return new State(id, pin.getState().isHigh());
        }
    }

    public Map<Integer, GpioPinDigitalOutput> getMap() {
        return map;
    }
}