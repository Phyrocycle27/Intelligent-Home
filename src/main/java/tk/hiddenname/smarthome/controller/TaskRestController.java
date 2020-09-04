package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.exception.*;
import tk.hiddenname.smarthome.repository.TaskRepository;
import tk.hiddenname.smarthome.service.task.TaskManager;

import javax.validation.Valid;

@RestController
@RequestMapping(value = {"/tasks"})
@AllArgsConstructor
public class TaskRestController {

    private static final Logger log = LoggerFactory.getLogger(TaskRestController.class);

    private final TaskRepository repo;
    private final TaskManager manager;

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Task create(@Valid @RequestBody Task task) throws NoSuchProcessorException, UnsupportedTriggerObjectTypeException,
            NoSuchListenerException, TriggerExistsException, ProcessorExistsException, UnsupportedProcessingObjectTypeException {

        task = repo.save(task);

        log.info(task.toString());
        manager.addTask(task);

        return task;
    }
}
