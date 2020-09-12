package tk.hiddenname.smarthome.utils.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import lombok.Getter;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GpioManager {

    private static final GpioController controller = GpioFactory.getInstance();
    private static final Set<Integer> digitalGPIO;
    private static final Set<Integer> pwmGPIO;

    static {
        digitalGPIO = new HashSet<Integer>() {{
            add(2);
            add(3);
            add(17);
            add(27);
            add(22);
            add(10);
            add(9);
            add(11);
            add(14);
            add(15);
            add(18);
            add(23);
            add(24);
            add(25);
            add(8);
            add(7);
            add(1);
            add(12);
            add(16);
            add(20);
            add(21);
            add(0);
            add(5);
            add(6);
            add(13);
            add(19);
            add(26);
        }};

        pwmGPIO = new HashSet<Integer>() {{
            add(13);
            add(19);
            add(18);
            add(12);
        }};
    }

    @Getter
    private final int pwmRange = 1024;
    private final List<Integer> usedGPIO = new ArrayList<>();

    public void deletePin(GpioPin pin) {
        controller.unprovisionPin(pin);
        usedGPIO.remove(Integer.valueOf(Gpio.wpiPinToGpio(pin.getPin().getAddress())));
        pin.removeAllListeners();
    }

    public void validate(Integer gpio, SignalType type) throws PinSignalSupportException, SignalTypeNotFoundException,
            GPIOBusyException {

        if (!isSupports(type, gpio)) {
            throw new PinSignalSupportException(gpio);
        }

        if (isExists(gpio))
            throw new GPIOBusyException(gpio);
    }

    private boolean isSupports(SignalType type, Integer gpio) {
        switch (type) {
            case DIGITAL:
                return digitalGPIO.contains(gpio);
            case PWM:
                return pwmGPIO.contains(gpio);
            default:
                return false;
        }
    }

    private boolean isExists(Integer gpio) {
        return usedGPIO.contains(gpio);
    }

    private Pin getPinByGPIONumber(int gpioNumber) {
        switch (gpioNumber) {
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

    public GpioPinDigitalOutput createDigitalOutput(Integer gpio, Boolean reverse)
            throws GPIOBusyException, PinSignalSupportException {

        validate(gpio, SignalType.DIGITAL);

        GpioPinDigitalOutput pin = controller.provisionDigitalOutputPin(
                getPinByGPIONumber(gpio), PinState.getState(reverse));

        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_OUTPUT);

        usedGPIO.add(gpio);

        return pin;
    }

    public GpioPinDigitalInput createDigitalInput(Integer gpio)
            throws GPIOBusyException, PinSignalSupportException {

        validate(gpio, SignalType.DIGITAL);

        GpioPinDigitalInput pin = controller.provisionDigitalInputPin(
                getPinByGPIONumber(gpio), PinPullResistance.PULL_DOWN);

        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_INPUT);

        usedGPIO.add(gpio);

        return pin;
    }

    public GpioPinPwmOutput createPwmOutput(Integer gpio, Boolean reverse)
            throws GPIOBusyException, PinSignalSupportException {
        validate(gpio, SignalType.PWM);

        GpioPinPwmOutput pin = controller.provisionPwmOutputPin(
                getPinByGPIONumber(gpio), reverse ? pwmRange : 0);

        pin.setPwmRange(pwmRange);
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        usedGPIO.add(gpio);

        return pin;
    }

    public Set<Integer> getAvailableDigitalGPIO() {
        Set<Integer> available = new HashSet<>();

        for (Integer gpio : digitalGPIO) {
            if (usedGPIO.contains(gpio)) continue;
            available.add(gpio);
        }
        return available;
    }

    public Set<Integer> getAvailablePwmGPIO() {
        Set<Integer> available = new HashSet<>();

        for (Integer gpio : pwmGPIO) {
            if (usedGPIO.contains(gpio)) continue;
            available.add(gpio);
        }
        return available;
    }

    public void shutdown() {
        controller.shutdown();
    }
}
