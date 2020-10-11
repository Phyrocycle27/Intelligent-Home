package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ProcessorExistsException extends Exception {

    public ProcessorExistsException(int id) {
        super(String.format("Processor with id '%d' has been already existed", id));
    }
}
