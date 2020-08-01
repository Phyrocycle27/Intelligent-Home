package tk.hiddenname.smarthome.exception;

public class UnsupportedSignalTypeException extends Exception {

    public UnsupportedSignalTypeException(String type) {
        super(String.format("The type '%s' unsupports!", type));
    }
}
