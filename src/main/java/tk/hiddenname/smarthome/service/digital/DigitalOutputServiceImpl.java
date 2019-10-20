package tk.hiddenname.smarthome.service.digital;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.Output;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.service.OutputService;
import tk.hiddenname.smarthome.utils.gpio.GPIO;
import tk.hiddenname.smarthome.utils.gpio.OutputController;

import java.util.HashMap;
import java.util.Map;

@Service
public class DigitalOutputServiceImpl implements OutputService, DigitalOutputService {

    private final OutputController controller;
    private static Map<Integer, GpioPinDigitalOutput> map;

    static {
        map = new HashMap<>();
    }

    @Autowired
    public DigitalOutputServiceImpl(OutputController controller) {
        this.controller = controller;
    }

    @Override
    public void delete(Integer id) {
        System.out.println("Curr map is: " + map);
        controller.setState(map.get(id), false);
        GPIO.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Output newOutput) {
        System.out.println("\n *Create method in DigitalOutputService");
        System.out.println("\tNew output is: " + newOutput);
        System.out.println("\tCurr map is: " + map);
        map.put(newOutput.getOutputId(), GPIO.createDigitalPin(
                newOutput.getGpio(),
                newOutput.getName(),
                newOutput.getReverse()
        ));
        System.out.println(map.get(newOutput.getOutputId()));
    }

    @Override
    public void update(Output newOutput) {
        System.out.println("\n *Update method in DigitalOutputService");
        System.out.println("\tNew output is: " + newOutput);
        System.out.println("\tCurr map is: " + map);
        map.get(newOutput.getOutputId()).setName(newOutput.getName());
        System.out.println("\tCurr map is (after setName): " + map);
        setState(newOutput, false);
    }

    @Override
    public DigitalState getState(Output output) {
        GpioPinDigitalOutput pin = map.getOrDefault(output.getOutputId(), null);

        if (pin == null) throw new OutputNotFoundException(output.getOutputId());

        else return new DigitalState(
                output.getOutputId(), output.getReverse() ^ pin.getState().isHigh()
        );
    }

    @Override
    public DigitalState setState(Output output, Boolean newState) {
        GpioPinDigitalOutput pin = map.getOrDefault(output.getOutputId(), null);

        if (pin == null) throw new OutputNotFoundException(output.getOutputId());

        else return new DigitalState(
                output.getOutputId(), output.getReverse() ^ controller
                .setState(pin, output.getReverse() ^ newState)
        );
    }

    public Map<Integer, GpioPinDigitalOutput> getMap() {
        return map;
    }
}