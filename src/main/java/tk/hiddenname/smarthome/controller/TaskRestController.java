package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.exception.*;
import tk.hiddenname.smarthome.service.database.TaskDatabaseService;
import tk.hiddenname.smarthome.service.task.TaskManager;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = {"/tasks"})
@AllArgsConstructor
public class TaskRestController {

    private final TaskDatabaseService service;
    private final TaskManager manager;

    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Task> getAll() {
        return service.getAll();
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Task create(@Valid @RequestBody Task task) throws NoSuchProcessorException, UnsupportedTriggerObjectTypeException,
            NoSuchListenerException, TriggerExistsException, ProcessorExistsException, UnsupportedProcessingObjectTypeException {
        task = service.create(task);
        manager.addTask(task);

        return task;
    }

    @DeleteMapping(value = {"/one/delete/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) throws TaskNotFoundException {
        manager.removeTask(id);
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
