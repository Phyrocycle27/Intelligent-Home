package tk.hiddenname.smarthome.exception;

public class TriggerExistsException extends RuntimeException {

    public TriggerExistsException(int id) {
        super(String.format("Trigger with id '%d' exists", id));
    }
}
