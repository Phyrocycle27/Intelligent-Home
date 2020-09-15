package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidProcessingObjectTypeException extends RuntimeException {

    public InvalidProcessingObjectTypeException() {
        super("Invalid ProcessingObject type");
    }
}