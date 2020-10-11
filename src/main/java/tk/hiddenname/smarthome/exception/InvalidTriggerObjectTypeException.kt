package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidTriggerObjectTypeException extends RuntimeException {

    public InvalidTriggerObjectTypeException() {
        super("Invalid TriggerObject type");
    }
}
