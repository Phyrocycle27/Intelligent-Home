package tk.hiddenname.smarthome.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entities.Control;
import tk.hiddenname.smarthome.repository.ControlRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("devices/control/db")
public class ControlDBController {
    private final ControlRepository repository;

    @Autowired
    public ControlDBController(ControlRepository controlRepository) {
        repository = controlRepository;
    }

    @GetMapping
    public List<Control> list() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    public Control getOne(@PathVariable("id") Control control) {
        return control;
    }

    @PostMapping
    public Control create(@RequestBody Control control) {
        control.setCreationDate(LocalDateTime.now());
        return repository.save(control);
    }

    @PutMapping("{id}")
    public Control update(@PathVariable("id") Control controlFromDb,
                          @RequestBody Control control) {
        BeanUtils.copyProperties(control, controlFromDb, "id");
        return repository.save(controlFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Control control) {
        repository.delete(control);
    }

}
