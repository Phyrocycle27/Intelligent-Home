package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TriggerNotFoundException extends Exception {

    public TriggerNotFoundException(int id) {
        super(String.format("The trigger with id '%d' not found", id));
    }
}
