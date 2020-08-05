package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SignalTypeNotFoundException extends RuntimeException {

    public SignalTypeNotFoundException(String type) {
        super(String.format("The type '%s' is invalid!", type));
    }
}
