package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SensorNotFoundException extends RuntimeException {

    public SensorNotFoundException(Long id) {
        super(String.format("The sensor with id '%d' not found", id));
    }
}
