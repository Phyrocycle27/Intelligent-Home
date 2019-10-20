package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OutputNotFoundException extends RuntimeException {

    public OutputNotFoundException(int id) {
        super(String.format("Not found output with id '%d'", id));
    }
}
