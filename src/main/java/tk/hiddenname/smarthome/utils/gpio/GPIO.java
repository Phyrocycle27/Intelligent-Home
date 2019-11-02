package tk.hiddenname.smarthome.utils.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import lombok.Getter;
import lombok.Setter;
import tk.hiddenname.smarthome.Application;
import tk.hiddenname.smarthome.exception.OutputAlreadyExistException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GPIO {

    @Getter
    @Setter
    private static Integer pwmRange;
    private static List<Integer> usedGpios;
    private static final Set<Integer> digitalGpios;
    private static final Set<Integer> pwmGpios;

    static {
        usedGpios = new ArrayList<>();
        digitalGpios = new HashSet<Integer>(){{
            add(4); add(17); add(18);add(27); add(22); add(23);
            add(24); add(5); add(6);add(13); add(19); add(26);
            add(12); add(16); add(20); add(21);
        }};

        pwmGpios = new HashSet<Integer>(){{
            add(23); add(24); add(18); add(12);
        }};
    }

    public static GpioPinDigitalOutput createDigitalPin(Integer gpio, String name, Boolean reverse)
            throws OutputAlreadyExistException, PinSignalSupportException {

        if (digitalGpios.contains(gpio)) {
            if (isExist(gpio)) throw new OutputAlreadyExistException(gpio);

            GpioPinDigitalOutput pin = Application.getGpioController().provisionDigitalOutputPin(
                    getPinByGPIONumber(gpio), name, PinState.getState(reverse));

            usedGpios.add(gpio);

            pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

            return pin;
        } else throw new PinSignalSupportException(gpio);


    }

    public static GpioPinPwmOutput createPwmPin(Integer gpio, String name, Boolean reverse)
            throws OutputAlreadyExistException, PinSignalSupportException {

        if (pwmGpios.contains(gpio)) {
            if (isExist(gpio)) throw new OutputAlreadyExistException(gpio);

            GpioPinPwmOutput pin = Application.getGpioController().provisionPwmOutputPin(
                    getPinByGPIONumber(gpio), name, reverse ? pwmRange : 0);

            pin.setPwmRange(pwmRange);

            usedGpios.add(gpio);

            pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

            return pin;
        } else throw new PinSignalSupportException(gpio);


    }

    public static void deletePin(GpioPin pin) {
        Application.getGpioController().unprovisionPin(pin);
        usedGpios.remove(Integer.valueOf(Gpio.wpiPinToGpio(pin.getPin().getAddress())));
    }

    private static boolean isExist(Integer gpio) {
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

    public Set<Integer> getAvailableDigitalGpios() {
        Set<Integer> gpios = new HashSet<>();

        for (Integer gpio : digitalGpios) {
            if(!usedGpios.contains(gpio)) gpios.add(gpio);
        }
        return gpios;
    }

    public Set<Integer> getAvailablePwmGpios() {
        Set<Integer> gpios = new HashSet<>();

        for (Integer gpio : pwmGpios) {
            if(!usedGpios.contains(gpio)) gpios.add(gpio);
        }
        return gpios;
    }
}
