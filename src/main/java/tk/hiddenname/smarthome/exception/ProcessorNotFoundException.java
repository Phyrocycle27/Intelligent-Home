package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProcessorNotFoundException extends Exception {

    public ProcessorNotFoundException(int id) {
        super(String.format("The processor with id '%d' not found", id));
    }
}
