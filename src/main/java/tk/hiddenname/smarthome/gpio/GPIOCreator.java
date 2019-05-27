package tk.hiddenname.smarthome.gpio;

import com.pi4j.io.gpio.*;

public class GPIOCreator {

    private GpioController controller;

    public GPIOCreator() {
        controller = GpioFactory.getInstance();
    }

    public GpioPinDigitalOutput getGPIO(int gpioNumber, String name, int state) {
        return controller.provisionDigitalOutputPin(getPinByGPIONumber(gpioNumber), name, PinState.getState(state));
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
}