package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class TriggerExistsException extends Exception {

    public TriggerExistsException(int id) {
        super(String.format("The trigger with id '%d' have been already existed", id));
    }
}
