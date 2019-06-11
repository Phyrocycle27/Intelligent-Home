package tk.hiddenname.smarthome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.hiddenname.smarthome.entities.Output;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.service.ControlOutputService;
import tk.hiddenname.smarthome.service.OutputService;

@RestController
public class ControlOutputController {

    private ControlOutputService service;
    private OutputService dataService;

    @Autowired
    private void setService(OutputService dataService) {
        this.dataService = dataService;
    }

    @Autowired
    public void setService(ControlOutputService service) {
        this.service = service;
    }

    @PutMapping("/control/{id}/signal={signal}")
    public ResponseEntity updateSignal(@PathVariable(value = "id") Integer outputId,
                                       @PathVariable(value = "signal") Integer signal) throws OutputNotFoundException {
        service.updateSignal(outputId, signal);
        Output output = dataService.getOutputById(outputId);
        output.setSignal(signal);
        dataService.saveOutput(output);
        return ResponseEntity.ok().build();
    }
}
