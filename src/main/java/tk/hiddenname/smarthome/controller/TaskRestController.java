package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.entity.task.processing.ProcessingAction;
import tk.hiddenname.smarthome.entity.task.processing.ProcessingGroup;
import tk.hiddenname.smarthome.entity.task.processing.objects.DeviceSetSignalObject;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.entity.task.trigger.TriggerAction;
import tk.hiddenname.smarthome.entity.task.trigger.TriggerGroup;
import tk.hiddenname.smarthome.entity.task.trigger.objects.SensorChangeSignalObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.repository.TaskRepository;
import tk.hiddenname.smarthome.service.TaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<ProcessingAction, ProcessingGroup> procGroup = new HashMap<>();
        {
            ProcessingGroup group = new ProcessingGroup();
            group.setAction(ProcessingAction.DEVICE_SET_SIGNAL);

            List<ProcessingObject> objects = new ArrayList<>();
            DeviceSetSignalObject object = new DeviceSetSignalObject(0, SignalType.DIGITAL, Boolean.toString(true));
            objects.add(object);
            group.setProcessingObjects(objects);
            procGroup.put(group.getAction(), group);
        }
        /* TriggerGroup */
        Map<TriggerAction, TriggerGroup> trigGroup = new HashMap<>();
        {
            TriggerGroup group = new TriggerGroup();
            group.setAction(TriggerAction.SENSOR_CHANGE_SIGNAL);

            List<TriggerObject> objects = new ArrayList<>();
            SensorChangeSignalObject object = new SensorChangeSignalObject(0, SignalType.DIGITAL, Boolean.toString(true));
            objects.add(object);
            group.setTriggerObjects(objects);
            trigGroup.put(group.getAction(), group);
        }

        task.setTriggerGroups(trigGroup);
        task.setProcessingGroups(procGroup);

        task = repo.save(task);

        return task;
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Task create(@RequestBody Task task) {
        log.info("************** POST method: /tasks/create ************************");

        task = repo.save(task);
        manager.add(task);

        return task;
    }
}
