package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.hiddenname.smarthome.entity.Task;
import tk.hiddenname.smarthome.entity.listening.ListeningAction;
import tk.hiddenname.smarthome.entity.listening.ListeningObject;
import tk.hiddenname.smarthome.entity.listening.SensorChangeSignalObject;
import tk.hiddenname.smarthome.entity.processing.DeviceSetSignalObject;
import tk.hiddenname.smarthome.entity.processing.ProcessingAction;
import tk.hiddenname.smarthome.entity.processing.ProcessingObject;
import tk.hiddenname.smarthome.entity.signal.SignalObject;
import tk.hiddenname.smarthome.entity.signal.SignalType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = {"/tasks"})
@AllArgsConstructor
public class TaskRestController {

    private static final Logger log = LoggerFactory.getLogger(TaskRestController.class);

    @GetMapping(value = {"/one/1"}, produces = {"application/json"})
    public Task getOne() {
        log.info("************** GET method: /tasks/one/1 ************************");
        Task task = Task.builder().id(13435)
                .name("simple")
                .build();
        Map<String, Set<ListeningObject>> listeningMap = new HashMap<>();
        Set<ListeningObject> listeningObjects = new HashSet<>();

        listeningObjects.add(new SensorChangeSignalObject(3, new SignalObject(
            SignalType.DIGITAL, 1
        )));
        listeningMap.put(ListeningAction.SENSOR_CHANGE_SIGNAL.name(), listeningObjects);

        Map<String, Set<ProcessingObject>> processingMap = new HashMap<>();
        Set<ProcessingObject> processingObjects = new HashSet<>();

        processingObjects.add(new DeviceSetSignalObject(4, new SignalObject(
                SignalType.PWM, 432
        )));
        processingMap.put(ProcessingAction.DEVICE_SET_SIGNAL.name(), processingObjects);

        task.setListeningObjects(listeningMap);
        task.setProcessingObjects(processingMap);

        return task;
    }
}
