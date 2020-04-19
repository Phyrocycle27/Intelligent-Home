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

public class GPIO {

    private static final Set<Integer> digitalGpios;
    private static final Set<Integer> pwmGpios;
    private static final List<Integer> usedGpios;

    @Getter
    @Setter
    private static Integer pwmRange;

    static {
        usedGpios = new ArrayList<>();
        digitalGpios = new HashSet<Integer>() {{
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

        pwmGpios = new HashSet<Integer>() {{
            add(13);
            add(19);
            add(18);
            add(12);
        }};

    }

    public static void deletePin(GpioPin pin) {
        Application.getGpioController().unprovisionPin(pin);
        usedGpios.remove(Integer.valueOf(Gpio.wpiPinToGpio(pin.getPin().getAddress())));
    }

    public static void validate(Integer gpio, String type) throws PinSignalSupportException, TypeNotFoundException,
            OutputAlreadyExistException {

        if (!isSupports(type, gpio)) {
            throw new PinSignalSupportException(gpio);
        }

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

    public static GpioPinDigitalOutput createDigitalPin(Integer gpio, String name, Boolean reverse)
            throws OutputAlreadyExistException, PinSignalSupportException {
        GPIO.validate(gpio, "digital");

        GpioPinDigitalOutput pin = Application.getGpioController()
                .provisionDigitalOutputPin(getPinByGPIONumber(gpio), name, PinState.getState(reverse));

        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        usedGpios.add(gpio);

        return pin;
    }

    public static GpioPinPwmOutput createPwmPin(Integer gpio, String name, Boolean reverse)
            throws OutputAlreadyExistException, PinSignalSupportException {
        GPIO.validate(gpio, "pwm");

        GpioPinPwmOutput pin = Application.getGpioController()
                .provisionPwmOutputPin(getPinByGPIONumber(gpio), name, reverse ? pwmRange : 0);

        pin.setPwmRange(pwmRange);
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        usedGpios.add(gpio);

        return pin;
    }

    public static Set<Integer> getAvailableDigitalGpios() {
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
