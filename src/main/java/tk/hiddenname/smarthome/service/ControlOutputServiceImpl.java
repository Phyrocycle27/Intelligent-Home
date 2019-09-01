package tk.hiddenname.smarthome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entities.Output;
import tk.hiddenname.smarthome.entities.Signal;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.gpio.Controller;
import tk.hiddenname.smarthome.gpio.Outputs;

@Service
public class ControlOutputServiceImpl implements ControlOutputService {

    private Outputs outputs;
    private Controller controller;
    private OutputService dataService;

    @Autowired
    private void setDataService(OutputService dataService) {
        this.dataService = dataService;
    }

    @Autowired
    public void setOutputs(Outputs outputs) {
        this.outputs = outputs;
    }

    @Autowired
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public Signal getSignal(Integer id) throws OutputNotFoundException {
        return new Signal(dataService.getOutputById(id).getSignal());
    }

    @Override
    public void updateSignal(Integer id, Signal signal) throws OutputNotFoundException {
        System.out.println("Updated signal on device with id " + id + " to " + signal + " ...");
        Object output = outputs.get(id);
        controller.setSignal(output, signal.getSignal());

        // Update in database
        Output outputFromDb = dataService.getOutputById(id);
        outputFromDb.setSignal(signal.getSignal());
        dataService.saveOutput(outputFromDb);
    }
}