package tk.hiddenname.smarthome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entities.Output;
import tk.hiddenname.smarthome.entities.OutputPojo;
import tk.hiddenname.smarthome.service.OutputService;

import java.util.List;

@RestController
@RequestMapping("devices/outputs/db")
public class OutputsDBController {

    private OutputService service;

    @Autowired
    public void setService(OutputService service) {
        this.service = service;
    }

    @GetMapping
    public List<Output> list() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public OutputPojo getOne(@PathVariable Integer id) {
        return service.getOutputById(id);
    }

    @PostMapping
    public OutputPojo create(@RequestBody Output output) {
        return service.saveOutput(output);
    }

    @PutMapping("{id}")
    public OutputPojo update(@PathVariable("id") Output outputFromDb,
                             @RequestBody Output output) {
        return service.updateOutput(output, outputFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        service.deleteOutput(id);
    }
}