package tk.hiddenname.smarthome.exception;

public class OutputNotFoundException extends RuntimeException {

    public OutputNotFoundException(String type, int id) {
        super(String.format("Not found %s output with id '%d'", type, id));
    }
}
