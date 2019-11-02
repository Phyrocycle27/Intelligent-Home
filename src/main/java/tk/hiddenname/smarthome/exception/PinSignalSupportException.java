package tk.hiddenname.smarthome.exception;

public class PinSignalSupportException extends Exception{

    public PinSignalSupportException(int gpio) {
        super(String.format("Gpio %d doesn't support the type of signal", gpio));
    }
}
