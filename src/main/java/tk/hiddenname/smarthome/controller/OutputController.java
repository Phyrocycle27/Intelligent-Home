package tk.hiddenname.smarthome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entities.Output;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.gpio.Outputs;
import tk.hiddenname.smarthome.service.OutputServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OutputController {

    private Outputs outputs;
    private OutputServiceImpl service;

    @Autowired
    public void setService(OutputServiceImpl service) {
        this.service = service;
    }

    @Autowired
    public void setOutputs(Outputs outputs) {
        this.outputs = outputs;
    }

    @GetMapping("/outputs")
    public List<Output> getAllNotes() {
        return service.getAllOutputs();
    }

    @PostMapping("/outputs")
    public Output createNote(@Valid @RequestBody Output output) {
        outputs.add(output);
        return service.saveOutput(output);
    }

    @GetMapping("/outputs/{id}")
    public Output getNoteById(@PathVariable(value = "id") Integer outputId) throws OutputNotFoundException {
        return service.getOutputById(outputId);
    }

    @PutMapping("/outputs/{id}")
    public Output updateNote(@PathVariable(value = "id") Integer outputId,
                             @Valid @RequestBody Output outputDetails) throws OutputNotFoundException {
        outputs.replace(outputId, outputDetails);
        return service.updateOutput(outputId, outputDetails);
    }

    @DeleteMapping("/outputs/{id}")
    public ResponseEntity<Output> deleteNote(@PathVariable(value = "id") Integer outputId) throws OutputNotFoundException {
        outputs.delete(outputId);
        service.deleteOutput(outputId);
        return ResponseEntity.ok().build();
    }
}