package tk.hiddenname.smarthome.service.pwm;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.Output;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.service.OutputService;
import tk.hiddenname.smarthome.utils.gpio.GPIO;
import tk.hiddenname.smarthome.utils.gpio.OutputController;

import java.util.HashMap;
import java.util.Map;

@Service
public class PwmOutputServiceImpl implements OutputService, PwmOutputService {

    private final OutputController controller;
    private static Map<Integer, GpioPinPwmOutput> map;

    static {
        map = new HashMap<>();
    }

    @Autowired
    public PwmOutputServiceImpl(OutputController controller) {
        this.controller = controller;
    }

    @Override
    public void delete(Integer id) {
        controller.setSignal(map.get(id), 0);
        GPIO.deletePin(map.get(id));
        map.remove(id);
    }

    @Override
    public void save(Output newOutput) {
        map.put(newOutput.getOutputId(), GPIO.createPwmPin(
                newOutput.getGpio(),
                newOutput.getName(),
                newOutput.getReverse()
        ));
    }

    @Override
    public void update(Output newOutput) {
        System.out.println("\nNew output is: "+newOutput);
        System.out.println("Updatable output's id is: "+ newOutput.getOutputId());
        System.out.println("Curr map is: " + map);
        map.get(newOutput.getOutputId()).setName(newOutput.getName());
        setSignal(newOutput, 0);
    }

    @Override
    public PwmSignal getSignal(Output output) {
        GpioPinPwmOutput pin = map.getOrDefault(output.getOutputId(), null);

        if (pin == null) throw new OutputNotFoundException(output.getOutputId());

        else {
            return new PwmSignal(output.getOutputId(), output.getReverse() ?
                    GPIO.getPwmRange() - pin.getPwm() : pin.getPwm());
        }
    }

    @Override
    public PwmSignal setSignal(Output output, Integer newSignal) {
        GpioPinPwmOutput pin = map.getOrDefault(output.getOutputId(), null);

        if (pin == null) throw new OutputNotFoundException(output.getOutputId());
        else {
            Integer currSignal = controller.setSignal(
                    map.get(output.getOutputId()), output.getReverse() ?
                            GPIO.getPwmRange() - newSignal : newSignal);

            return new PwmSignal(output.getOutputId(), output.getReverse() ?
                    GPIO.getPwmRange() - currSignal : currSignal);
        }
    }

    public Map<Integer, GpioPinPwmOutput> getMap() {
        return map;
    }
}
