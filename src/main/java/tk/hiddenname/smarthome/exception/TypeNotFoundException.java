package tk.hiddenname.smarthome.exception;

public class TypeNotFoundException extends RuntimeException {

    public TypeNotFoundException(String type) {
        super(String.format("Type %s is invalid!", type));
    }
}
