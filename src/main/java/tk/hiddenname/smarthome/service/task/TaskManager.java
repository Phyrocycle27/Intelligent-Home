package tk.hiddenname.smarthome.service.task;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.service.task.impl.TaskObject;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class TaskManager {

    private final Map<Integer, TaskObject> tasks = new HashMap<>();

    private final ApplicationContext context;

    public void addTask(Task task) {
        TaskObject taskObject = context.getBean(TaskObject.class);
        tasks.put(task.getId(), taskObject.register(task));
    }
}
