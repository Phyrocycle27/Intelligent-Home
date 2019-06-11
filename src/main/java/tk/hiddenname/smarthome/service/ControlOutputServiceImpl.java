package tk.hiddenname.smarthome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.gpio.Outputs;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;

@Service
public class ControlOutputServiceImpl implements ControlOutputService {

    private Outputs outputs;

    @Autowired
    public void setOutputs(Outputs outputs) {
        this.outputs = outputs;
    }

    @Override
    public void updateSignal(Integer id, Integer signal) throws OutputNotFoundException {
        // меняем сигнал на выходе
    }
}
