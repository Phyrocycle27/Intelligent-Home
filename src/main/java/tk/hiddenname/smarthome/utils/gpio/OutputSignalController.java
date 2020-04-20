package tk.hiddenname.smarthome.utils.gpio;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.Gpio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OutputSignalController {

    public static final Logger log = LoggerFactory.getLogger(OutputSignalController.class);

    public Boolean setState(GpioPinDigitalOutput output, Boolean state) {
        Integer pin = Gpio.wpiPinToGpio(output.getPin().getAddress());

        PinState currState = output.getState();
        PinState newState = PinState.getState(state);

        if (newState.equals(currState))
            log.debug("Pin was already " + state);
        else {
            log.debug(String.format("GPIO %d status before: %s\n", pin, currState));
            output.setState(newState);
            log.debug(String.format("GPIO %d status after: %s\n", pin, output.getState()));
        }
        return output.getState().isHigh();
    }

    public Integer setSignal(GpioPinPwmOutput output, Integer signal) {
        Integer pin = Gpio.wpiPinToGpio(output.getPin().getAddress());

        Integer currSignal = output.getPwm();

        if (signal.equals(currSignal))
            log.debug("Pin was already " + signal);
        else {
            log.debug(String.format("GPIO %d signal before: %s\n", pin, currSignal));
            output.setPwm(signal);
            log.debug(String.format("GPIO %d signal after: %s\n", pin, output.getPwm()));
        }
        return output.getPwm();
    }
}