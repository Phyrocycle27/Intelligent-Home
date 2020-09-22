package tk.hiddenname.smarthome.service.database;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.repository.TaskRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskDatabaseService {

    private final TaskRepository repo;

    public List<Task> getAll() {
        return repo.findAll(Sort.by("id"));
    }

    public Task create(Task newTask) {
        return repo.save(newTask);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
