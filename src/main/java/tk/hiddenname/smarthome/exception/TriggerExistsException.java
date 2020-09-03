package tk.hiddenname.smarthome.exception;

public class TriggerExistsException extends Exception {

    public TriggerExistsException(int id) {
        super(String.format("Trigger with id '%d' exists", id));
    }
}
