package tk.hiddenname.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends Exception {

    public TaskNotFoundException(Integer id) {
        super(String.format("The task with id '%d' not found", id));
    }
}
