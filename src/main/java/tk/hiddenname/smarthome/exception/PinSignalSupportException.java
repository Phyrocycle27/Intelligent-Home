package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PinSignalSupportException extends Exception{

    public PinSignalSupportException(int gpio) {
        super(String.format("Gpio '%d' doesn't support this type of signal", gpio));
    }
}
