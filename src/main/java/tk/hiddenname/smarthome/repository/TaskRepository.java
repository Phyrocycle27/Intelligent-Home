package tk.hiddenname.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.hiddenname.smarthome.entity.task.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
