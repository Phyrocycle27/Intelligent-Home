package tk.hiddenname.smarthome.utils.gpio;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OutputSignalController {

    public static final Logger log = LoggerFactory.getLogger(OutputSignalController.class);

    public Boolean setState(GpioPinDigitalOutput output, Boolean state) {
        String pinName = output.getName();

        PinState currState = output.getState();
        PinState newState = PinState.getState(state);

        if (newState.equals(currState))
            log.debug("Pin was already " + state);
        else {
            log.debug(String.format("Digital output's \"%s\" status before: %s\n", pinName, currState));
            output.setState(newState);
            log.debug(String.format("Digital output's \"%s\" status after: %s\n", pinName, output.getState()));
        }
        return output.getState().isHigh();
    }

    public Integer setSignal(GpioPinPwmOutput output, Integer signal) {
        String pinName = output.getName();

        Integer currSignal = output.getPwm();

        if (signal.equals(currSignal))
            log.debug("Pin was already " + signal);
        else {
            log.debug(String.format("Pwm output's \"%s\" status before: %s\n", pinName, currSignal));
            output.setPwm(signal);
            log.debug(String.format("Pwm output's \"%s\" status after: %s\n", pinName, output.getPwm()));
        }
        return output.getPwm();
    }
}