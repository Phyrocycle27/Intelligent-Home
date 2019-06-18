package tk.hiddenname.smarthome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.hiddenname.smarthome.entities.Output;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.service.ControlOutputService;
import tk.hiddenname.smarthome.service.OutputService;

@RestController
public class ControlOutputController {

    private ControlOutputService controlService;
    private OutputService dataService;

    @Autowired
    private void setDataService(OutputService dataService) {
        this.dataService = dataService;
    }

    @Autowired
    public void setService(ControlOutputService controlService) {
        this.controlService = controlService;
    }

    @GetMapping("/control/{id}")
    public String getSignal(@PathVariable(value = "id") Integer id) throws OutputNotFoundException {
        return "{\"currentSignal\":".concat(dataService.getOutputById(id).getSignal().toString()).concat("}");
    }

    /*@GetMapping("/control/{id}/signal={signal}")
    public ResponseEntity setSignal(@PathVariable(value = "id") Integer outputId,
                                       @PathVariable(value = "signal") Integer signal) throws OutputNotFoundException {
        controlService.updateSignal(outputId, signal);
        Output output = dataService.getOutputById(outputId);
        output.setSignal(signal);
        dataService.saveOutput(output);
        return ResponseEntity.ok().build();
    }*/

    @PutMapping("/control/{id}/signal={signal}")
    public ResponseEntity updateSignal(@PathVariable(value = "id") Integer outputId,
                                       @PathVariable(value = "signal") Integer signal) throws OutputNotFoundException {
        controlService.updateSignal(outputId, signal);
        Output output = dataService.getOutputById(outputId);
        output.setSignal(signal);
        dataService.saveOutput(output);
        return ResponseEntity.ok().build();
    }
}
