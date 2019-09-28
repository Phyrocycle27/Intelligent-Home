package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class TypeNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(TypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String typeNotFoundHandler(TypeNotFoundException ex) {
        return ex.getMessage();
    }
}
