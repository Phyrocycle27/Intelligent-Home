package tk.hiddenname.smarthome.utils.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import lombok.Getter;
import lombok.Setter;
import tk.hiddenname.smarthome.Application;
import tk.hiddenname.smarthome.exception.OutputAlreadyExistException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.TypeNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class GPIO {

    private static final Logger LOGGER;
    private static final Set<Integer> digitalGpios;
    private static final Set<Integer> pwmGpios;
    private static List<Integer> usedGpios;

    @Getter
    @Setter
    private static Integer pwmRange;

    static {
        usedGpios = new ArrayList<>();
        digitalGpios = new HashSet<Integer>() {{
            add(4);
            add(17);
            add(18);
            add(27);
            add(22);
            add(23);
            add(24);
            add(5);
            add(6);
            add(13);
            add(19);
            add(26);
            add(12);
            add(16);
            add(20);
            add(21);
        }};

        pwmGpios = new HashSet<Integer>() {{
            add(23);
            add(24);
            add(18);
            add(12);
        }};

        LOGGER = Logger.getLogger(GPIO.class.getName());
    }

    public static void deletePin(GpioPin pin) {
        Application.getGpioController().unprovisionPin(pin);
        usedGpios.remove(Integer.valueOf(Gpio.wpiPinToGpio(pin.getPin().getAddress())));
    }

    public static void validate(Integer gpio, String type) throws PinSignalSupportException, TypeNotFoundException,
            OutputAlreadyExistException {

        if (!isSupports(type, gpio))
            throw new PinSignalSupportException(gpio);

        if (isExists(gpio))
            throw new OutputAlreadyExistException(gpio);
    }

    public static boolean isType(String type) {
        switch (type) {
            case "digital":
            case "pwm":
                return true;
            default:
                return false;
        }
    }

    private static boolean isSupports(String type, Integer gpio) {
        switch (type) {
            case "digital":
                return digitalGpios.contains(gpio);
            case "pwm":
                return pwmGpios.contains(gpio);
            default:
                return false;
        }
    }

    private static boolean isExists(Integer gpio) {
        return usedGpios.contains(gpio);
    }

    private static Pin getPinByGPIONumber(int gpioNumber) {
        switch (gpioNumber) {
            case 4:
                return RaspiPin.GPIO_07;
            case 17:
                return RaspiPin.GPIO_00;
            case 27:
                return RaspiPin.GPIO_02;
            case 22:
                return RaspiPin.GPIO_03;
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
            case 18:
                return RaspiPin.GPIO_01;
            case 23:
                return RaspiPin.GPIO_04;
            case 24:
                return RaspiPin.GPIO_05;
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

    public static GpioPinDigitalOutput createDigitalPin(Integer gpio, String name, Boolean reverse)
            throws OutputAlreadyExistException, PinSignalSupportException {
        GPIO.validate(gpio, "digital");

        GpioPinDigitalOutput pin = Application.getGpioController().provisionDigitalOutputPin(
                getPinByGPIONumber(gpio), name, PinState.getState(reverse));

        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        usedGpios.add(gpio);

        return pin;
    }

    public static GpioPinPwmOutput createPwmPin(Integer gpio, String name, Boolean reverse)
            throws OutputAlreadyExistException, PinSignalSupportException {
        GPIO.validate(gpio, "pwm");

        GpioPinPwmOutput pin = Application.getGpioController().provisionPwmOutputPin(
                getPinByGPIONumber(gpio), name, reverse ? pwmRange : 0);

        pin.setPwmRange(pwmRange);
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        usedGpios.add(gpio);

        return pin;
    }

    public static  Set<Integer> getAvailableDigitalGpios() {
        Set<Integer> available = new HashSet<>();

        for (Integer gpio : digitalGpios) {
            if (usedGpios.contains(gpio)) continue;
            available.add(gpio);
        }
        return available;
    }

    public static Set<Integer> getAvailablePwmGpios() {
        Set<Integer> available = new HashSet<>();

        for (Integer gpio : pwmGpios) {
            if (usedGpios.contains(gpio)) continue;
            available.add(gpio);
        }
        return available;
    }
}
