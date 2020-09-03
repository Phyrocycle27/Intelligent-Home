package tk.hiddenname.smarthome.exception;

public class TriggerNotFoundException extends Exception {

    public TriggerNotFoundException(int id) {
        super(String.format("Not found trigger with id '%d'", id));
    }
}
