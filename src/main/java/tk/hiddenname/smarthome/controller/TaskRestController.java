package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.exception.*;
import tk.hiddenname.smarthome.repository.TaskRepository;
import tk.hiddenname.smarthome.service.task.TaskManager;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = {"/tasks"})
@AllArgsConstructor
public class TaskRestController {

    private final TaskRepository repo;
    private final TaskManager manager;

    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Task> getAll() {
        return repo.findAll();
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Task create(@Valid @RequestBody Task task) throws NoSuchProcessorException, UnsupportedTriggerObjectTypeException,
            NoSuchListenerException, TriggerExistsException, ProcessorExistsException, UnsupportedProcessingObjectTypeException {
        task = repo.save(task);
        manager.addTask(task);

        return task;
    }

    @DeleteMapping(value = {"/one/delete/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) throws TaskNotFoundException {
        manager.removeTask(id);
        repo.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
