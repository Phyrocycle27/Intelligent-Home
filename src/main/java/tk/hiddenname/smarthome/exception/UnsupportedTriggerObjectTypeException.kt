package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnsupportedTriggerObjectTypeException extends Exception {

    public UnsupportedTriggerObjectTypeException(String s) {
        super(String.format("The type (%s) of the trigger object not supported by listeners", s));
    }
}
