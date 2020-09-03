package tk.hiddenname.smarthome.exception;

public class ProcessorExistsException extends Exception {

    public ProcessorExistsException(int id) {
        super(String.format("Processor with id '%d' exists", id));
    }
}
