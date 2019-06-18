package tk.hiddenname.smarthome.gpio;

import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinState;
import org.springframework.stereotype.Component;

@Component
public class Controller {

    public void setSignal(Object output, Integer signal) {
        if (output instanceof GpioPinDigitalOutput) {
            GpioPinDigitalOutput pin = (GpioPinDigitalOutput) output;
            String pinName = pin.getName();
            PinState state = PinState.getState(signal >= 1);
            PinState currState = pin.getState();

            if (state != currState) {
                System.out.println("Digital output's \"" + pinName + "\" status before: ".concat(currState.toString()));
                pin.setState(state);
                System.out.println("Digital output's \"" + pinName + "\" status after: ".concat(pin.getState().toString()));
            } else System.out.println("Pin have been already ".concat(state.toString()));

        } else if (output instanceof GpioPinAnalogOutput) {
            GpioPinAnalogOutput pin = (GpioPinAnalogOutput) output;
            String pinName = pin.getName();
            double currVal = pin.getValue();

            System.out.println("Analog output's \"" + pinName + "\" signal before: ".concat(String.valueOf(pin.getValue())));
            pin.setValue(signal);
            System.out.println("Analog output's \"" + pinName + "\" signal after: ".concat(String.valueOf(pin.getValue())));

        } else if (output instanceof GpioPinPwmOutput) {
            GpioPinPwmOutput pin = (GpioPinPwmOutput) output;
            String pinName = pin.getName();

            System.out.println("PWM output's \"" + pinName + "\" signal before: ".concat(String.valueOf(pin.getPwm())));
            pin.setPwm(signal);
            System.out.println("PWM output's \"" + pinName + "\" signal after: ".concat(String.valueOf(pin.getPwm())));
        }
    }
}