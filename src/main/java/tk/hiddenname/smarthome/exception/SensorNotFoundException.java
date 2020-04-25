package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SensorNotFoundException extends RuntimeException {

    public SensorNotFoundException(int id) {
        super(String.format("Not found sensor with id '%d'", id));
    }
}
