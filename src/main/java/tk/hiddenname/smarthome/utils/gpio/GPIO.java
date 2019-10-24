package tk.hiddenname.smarthome.utils.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import lombok.Getter;
import lombok.Setter;
import tk.hiddenname.smarthome.Application;

import java.util.ArrayList;
import java.util.List;

public class GPIO {

    @Getter
    @Setter
    private static Integer pwmRange;
    private static List<Integer> usedGpios = new ArrayList<>();

    public static GpioPinDigitalOutput createDigitalPin(Integer gpio, String name, Boolean reverse) {

        GpioPinDigitalOutput pin = Application.getGpioController().provisionDigitalOutputPin(
                getPinByGPIONumber(gpio), name, PinState.getState(reverse));

        usedGpios.add(gpio);

        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        return pin;
    }

    public static GpioPinPwmOutput createPwmPin(Integer gpio, String name, Boolean reverse) {

        GpioPinPwmOutput pin = Application.getGpioController().provisionPwmOutputPin(
                getPinByGPIONumber(gpio), name, reverse ? pwmRange : 0
        );
        pin.setPwmRange(pwmRange);

        usedGpios.add(gpio);

        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        return pin;
    }

    public static void deletePin(GpioPin pin) {
        Application.getGpioController().unprovisionPin(pin);
        usedGpios.remove(Integer.valueOf(Gpio.wpiPinToGpio(pin.getPin().getAddress())));
    }

    public static boolean isExist(Integer gpio) {
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
}
