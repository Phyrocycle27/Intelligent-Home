package tk.hiddenname.smarthome.utils.gpio;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinState;
import org.springframework.stereotype.Component;

@Component
public class OutputController {

    public Boolean setState(GpioPinDigitalOutput output, Boolean state) {
        String pinName = output.getName();

        PinState currState = output.getState();
        PinState newState = PinState.getState(state);

        if (newState.equals(currState)) {
            System.out.printf("Pin was already %s", state);
        } else {
            System.out.printf("Digital output's \"%s\" status before: %s\n", pinName, currState);
            output.setState(newState);
            System.out.printf("Digital output's \"%s\" status after: %s\n", pinName, output.getState());

        }
        return output.getState().isHigh();
    }

    public Integer setSignal(GpioPinPwmOutput output, Integer signal) {
        String pinName = output.getName();

        Integer currSignal = output.getPwm();

        if (signal.equals(currSignal)) {
            System.out.printf("Pin was already %s", signal);
        } else {
            System.out.printf("Pwm output's \"%s\" status before: %s\n", pinName, currSignal);
            output.setPwm(signal);
            System.out.printf("Pwm output's \"%s\" status after: %s\n", pinName, output.getPwm());

        }
        return output.getPwm();
    }
}