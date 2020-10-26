package tk.hiddenname.smarthome.service.database

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.model.task.Task
import tk.hiddenname.smarthome.repository.TaskRepository

@Service
class TaskDatabaseService(private val repo: TaskRepository) {

    fun getAll(): List<Task> = repo.findAll(Sort.by("id"))

    fun create(newTask: Task): Task = repo.save(newTask)

    fun delete(id: Long) = repo.deleteById(id)

    fun getNextId() = repo.getNextId()
}