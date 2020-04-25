package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class GPIOBusyException extends Exception {

    public GPIOBusyException(int gpio) {
        super(String.format("GPIO '%d' have already busied", gpio));
    }
}
