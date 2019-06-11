package tk.hiddenname.smarthome.exception;

public class OutputNotFoundException extends Exception{
    private int outputId;

    public OutputNotFoundException(int outputId) {
        super(String.format("Output is not found with id : '%s'", outputId));
    }
}
