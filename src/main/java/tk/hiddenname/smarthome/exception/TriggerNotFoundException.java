package tk.hiddenname.smarthome.exception;

public class TriggerNotFoundException extends RuntimeException {

    public TriggerNotFoundException(int id) {
        super(String.format("Not found trigger with id '%d'", id));
    }
}
