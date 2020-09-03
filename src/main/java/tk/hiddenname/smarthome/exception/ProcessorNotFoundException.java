package tk.hiddenname.smarthome.exception;

public class ProcessorNotFoundException extends Exception {

    public ProcessorNotFoundException(int id) {
        super(String.format("Not found processor with id '%d'", id));
    }
}
