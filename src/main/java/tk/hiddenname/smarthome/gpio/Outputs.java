package tk.hiddenname.smarthome.gpio;

import com.pi4j.io.gpio.*;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entities.Output;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@Component
public class Outputs {
    private static final String PWM = "pwm";
    private static final String ANALOG = "analog";
    private static final String DIGITAL = "digital";

    private Map<Integer, GpioPinAnalogOutput> analogOutputs;
    private Map<Integer, GpioPinDigitalOutput> digitalOutputs;
    private Map<Integer, GpioPinPwmOutput> pwmOutputs;

    private GpioController controller;

    @PostConstruct
    public void init() {
        analogOutputs = new HashMap<>();
        digitalOutputs = new HashMap<>();
        pwmOutputs = new HashMap<>();

        controller = GpioFactory.getInstance();
    }

    @PreDestroy
    public void destroy() {
        controller.shutdown();
    }

    public void add(Output output) {
        switch (output.getType()) {
            case PWM:
                System.out.println("pwm gpio add..");
                pwmOutputs.put(
                        output.getId(),
                        controller.provisionPwmOutputPin(
                                getPinByGPIONumber(output.getGpioNumber()), output.getName(), 0));
                break;
            case DIGITAL:
                System.out.println("digital gpio add...");
                digitalOutputs.put(
                        output.getId(),
                        controller.provisionDigitalOutputPin(
                                getPinByGPIONumber(output.getGpioNumber()), output.getName(), PinState.LOW));
                break;
            case ANALOG:
                System.out.println("analog gpio add...");
                analogOutputs.put(
                        output.getId(),
                        controller.provisionAnalogOutputPin(
                                getPinByGPIONumber(output.getGpioNumber()), output.getName(), 0.0));
                break;
        }
    }

    private Map getMap(Integer id) throws OutputNotFoundException {
        if (analogOutputs.containsKey(id)) {
            return analogOutputs;
        } else if (digitalOutputs.containsKey(id)) {
            return digitalOutputs;
        } else if (pwmOutputs.containsKey(id)) {
            return pwmOutputs;
        } else throw new OutputNotFoundException(id);
    }

    public Object get(Integer id) throws OutputNotFoundException {
        return getMap(id).get(id);
    }

    public void delete(Integer id) throws OutputNotFoundException {
        getMap(id).remove(id);
    }

    public void replace(Integer id, Output output) throws OutputNotFoundException {
        delete(id);
        add(output);
    }

    private Pin getPinByGPIONumber(int pinNumber) {
        switch (pinNumber) {
            case 2:
                return RaspiPin.GPIO_08;
            case 3:
                return RaspiPin.GPIO_09;
            case 4:
                return RaspiPin.GPIO_07;
            case 17:
                return RaspiPin.GPIO_00;
            case 27:
                return RaspiPin.GPIO_02;
            case 22:
                return RaspiPin.GPIO_03;
            case 10:
                return RaspiPin.GPIO_12;
            case 9:
                return RaspiPin.GPIO_13;
            case 11:
                return RaspiPin.GPIO_14;
            case 0:
                return RaspiPin.GPIO_30;
            case 5:
                return RaspiPin.GPIO_21;
            case 6:
                return RaspiPin.GPIO_22;
            case 13:
                return RaspiPin.GPIO_23;
            case 19:
                return RaspiPin.GPIO_24;
            case 26:
                return RaspiPin.GPIO_25;
            case 14:
                return RaspiPin.GPIO_15;
            case 15:
                return RaspiPin.GPIO_16;
            case 18:
                return RaspiPin.GPIO_01;
            case 23:
                return RaspiPin.GPIO_04;
            case 24:
                return RaspiPin.GPIO_05;
            case 25:
                return RaspiPin.GPIO_06;
            case 8:
                return RaspiPin.GPIO_10;
            case 7:
                return RaspiPin.GPIO_11;
            case 1:
                return RaspiPin.GPIO_31;
            case 12:
                return RaspiPin.GPIO_26;
            case 16:
                return RaspiPin.GPIO_27;
            case 20:
                return RaspiPin.GPIO_28;
            case 21:
                return RaspiPin.GPIO_29;
            default:
                return null;
        }
    }
}
