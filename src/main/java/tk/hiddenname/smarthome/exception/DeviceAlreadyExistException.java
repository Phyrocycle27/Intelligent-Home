package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DeviceAlreadyExistException extends Exception {

    public DeviceAlreadyExistException(int gpio) {
        super(String.format("The output with gpio '%d' have already created", gpio));
    }
}
