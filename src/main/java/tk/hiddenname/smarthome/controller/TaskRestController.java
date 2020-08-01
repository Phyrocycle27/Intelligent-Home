package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.entity.task.processing.ProcessingAction;
import tk.hiddenname.smarthome.entity.task.processing.objects.DeviceSetSignalObject;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.entity.task.trigger.TriggerAction;
import tk.hiddenname.smarthome.entity.task.trigger.objects.SensorChangeSignalObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.repository.TaskRepository;
import tk.hiddenname.smarthome.service.task.TaskManager;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = {"/tasks"})
@AllArgsConstructor
public class TaskRestController {

    private static final Logger log = LoggerFactory.getLogger(TaskRestController.class);

    private final TaskRepository repo;
    private final TaskManager manager;

    @GetMapping(value = {"/one/test"}, produces = {"application/json"})
    public Task getOne() {
        log.info("************** GET method: /tasks/one/test ************************");
        Task task = new Task();

        task.setName("test");

        /* ProcessingGroup */
        Set<ProcessingObject> processingObjects = new HashSet<>();
        {
            DeviceSetSignalObject object = new DeviceSetSignalObject(ProcessingAction.DEVICE_SET_SIGNAL, 0,
                    SignalType.DIGITAL, Boolean.toString(true));
            processingObjects.add(object);
        }
        /* TriggerGroup */
        Set<TriggerObject> triggerObjects = new HashSet<>();
        {
            SensorChangeSignalObject object = new SensorChangeSignalObject(TriggerAction.SENSOR_CHANGE_SIGNAL, 0,
                    SignalType.DIGITAL, Boolean.toString(true));
            triggerObjects.add(object);
        }

        task.setProcessingObjects(processingObjects);
        task.setTriggerObjects(triggerObjects);

        task = repo.save(task);

        return task;
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Task create(@RequestBody Task task) {
        log.info("************** POST method: /tasks/create ************************");

        task = repo.save(task);

        log.info(task.toString());
        manager.add(task);

        return task;
    }
}
