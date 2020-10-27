package tk.hiddenname.smarthome.service.database

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.DeviceNotFoundException
import tk.hiddenname.smarthome.exception.TaskNotFoundException
import tk.hiddenname.smarthome.model.task.Task
import tk.hiddenname.smarthome.repository.TaskRepository

@Service
class TaskDatabaseService(private val repo: TaskRepository) {

    fun getAll(): List<Task> = repo.findAll(Sort.by("id"))

    @Throws(TaskNotFoundException::class)
    fun getOne(id: Long): Task {
        return repo.findById(id).orElseThrow { TaskNotFoundException(id) }
    }

    fun create(newTask: Task): Task = repo.save(newTask)

    @Throws(TaskNotFoundException::class)
    fun delete(id: Long) {
        repo.delete(repo.findById(id).orElseThrow { DeviceNotFoundException(id) })
    }

    fun getNextId() = repo.getNextId()
}