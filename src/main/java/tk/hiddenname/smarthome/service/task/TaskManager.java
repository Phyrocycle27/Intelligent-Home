package tk.hiddenname.smarthome.service.task;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.exception.*;
import tk.hiddenname.smarthome.repository.TaskRepository;
import tk.hiddenname.smarthome.service.task.impl.TaskObject;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class TaskManager {

    private static final Logger log = LoggerFactory.getLogger(TaskManager.class);

    private final Map<Integer, TaskObject> tasks = new HashMap<>();

    private final ApplicationContext context;
    private final TaskRepository repo;

    public void addTask(Task task) throws TriggerExistsException, UnsupportedTriggerObjectTypeException, NoSuchListenerException,
            NoSuchProcessorException, ProcessorExistsException, UnsupportedProcessingObjectTypeException {

        TaskObject taskObject = context.getBean(TaskObject.class);
        tasks.put(task.getId(), taskObject.register(task));
    }

    public void removeTask(Integer taskId) throws TaskNotFoundException {
        if (tasks.containsKey(taskId)) {
            tasks.get(taskId).unregister();
        } else {
            throw new TaskNotFoundException(taskId);
        }
    }

    public void loadTasks() {
        for (Task task: repo.findAll()) {
            try {
                addTask(task);
            } catch (TriggerExistsException | UnsupportedTriggerObjectTypeException | NoSuchListenerException
                    | NoSuchProcessorException | ProcessorExistsException | UnsupportedProcessingObjectTypeException e) {
                log.error(e.getMessage());
            }
        }
    }
}
