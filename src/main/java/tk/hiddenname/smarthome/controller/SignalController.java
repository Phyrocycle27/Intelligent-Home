package tk.hiddenname.smarthome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entities.Signal;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.service.ControlOutputService;

import javax.validation.Valid;

@RestController
public class SignalController {

    private ControlOutputService controlService;

    @Autowired
    public void setService(ControlOutputService controlService) {
        this.controlService = controlService;
    }

    @GetMapping("/control/{id}")
    public Signal getSignal(@PathVariable(value = "id") Integer id) throws OutputNotFoundException {
        return controlService.getSignal(id);
    }

    @PutMapping("/control/{id}")
    public Signal updateSignal(@PathVariable(value = "id") Integer id,
                               @Valid @RequestBody Signal signal) throws OutputNotFoundException {
        controlService.updateSignal(id, signal);
        return controlService.getSignal(id);
    }
}