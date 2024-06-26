package tk.hiddenname.smarthome.service.task

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.not_found.TaskNotFoundException
import tk.hiddenname.smarthome.model.task.Task
import tk.hiddenname.smarthome.service.database.TaskDatabaseService
import tk.hiddenname.smarthome.service.task.impl.TaskManager

@Service
class TaskService(private val context: ApplicationContext,
                  private val service: TaskDatabaseService) {

    private val log = LoggerFactory.getLogger(TaskService::class.java)

    private val tasks = HashMap<Long, TaskManager>()

    fun addTask(task: Task) {
        val taskManager = context.getBean(TaskManager::class.java)
        tasks[task.id] = taskManager.register(task)
    }

    fun removeTask(taskId: Long) {
        tasks[taskId]?.unregister() ?: throw TaskNotFoundException(taskId)
    }

    fun loadTasks() {
        service.getAll().iterator().forEach {
            try {
                addTask(it)
            } catch (e: Exception) {
                log.error(e.message)
            }
        }
    }
}