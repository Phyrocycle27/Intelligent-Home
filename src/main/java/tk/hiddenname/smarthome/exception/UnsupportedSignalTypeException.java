package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import tk.hiddenname.smarthome.entity.signal.SignalType;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UnsupportedSignalTypeException extends RuntimeException {

    public UnsupportedSignalTypeException(SignalType type) {
        super(String.format("Signal type \"%s\" exists but not supported by your current device's configuration",
                type.toString()));
    }
}
