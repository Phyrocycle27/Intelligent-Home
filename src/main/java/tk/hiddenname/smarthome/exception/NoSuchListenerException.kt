package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class NoSuchListenerException extends Exception {

    public NoSuchListenerException() {
        super("Listener for the trigger object not found");
    }
}
