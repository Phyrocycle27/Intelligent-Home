package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class OutputNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(OutputNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String outputNotFoundHandler(OutputNotFoundException ex) {
        return ex.getMessage();
    }
}
