package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class NoSuchProcessorException extends Exception {

    public NoSuchProcessorException() {
        super("Processor for the processing object not found");
    }
}
