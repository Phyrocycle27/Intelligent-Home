package tk.hiddenname.smarthome.service;

import tk.hiddenname.smarthome.entities.Output;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;

import java.util.List;

public interface OutputService {
    List<Output> getAllOutputs();

    Output getOutputById(Integer id) throws OutputNotFoundException;

    Output saveOutput(Output output);

    Output updateOutput(Integer id, Output outputDetails) throws OutputNotFoundException;

    void deleteOutput(Integer id) throws OutputNotFoundException;
}
