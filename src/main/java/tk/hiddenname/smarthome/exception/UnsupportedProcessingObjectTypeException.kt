package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnsupportedProcessingObjectTypeException extends Exception {

    public UnsupportedProcessingObjectTypeException(String s) {
        super(String.format("The type (%s) of the processing object not supported by processors", s));
    }
}
