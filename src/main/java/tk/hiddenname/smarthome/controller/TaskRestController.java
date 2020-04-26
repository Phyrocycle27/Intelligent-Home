package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.hiddenname.smarthome.entity.SensorChangeSignalObject;
import tk.hiddenname.smarthome.entity.Task;

@RestController
@RequestMapping(value = {"/tasks"})
@AllArgsConstructor
public class TaskRestController {

    private static final Logger log = LoggerFactory.getLogger(TaskRestController.class);

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Task create(@RequestBody Task task) {
        log.info("************** POST method: /devices/create ************************");
        log.info("Creating device is " + task.toString());

        SensorChangeSignalObject obj = (SensorChangeSignalObject) task.getListeningObject();
        log.info(obj.toString());

        return task;
    }
}
