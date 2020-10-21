package tk.hiddenname.smarthome.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.exception.*
import tk.hiddenname.smarthome.model.task.Task
import tk.hiddenname.smarthome.service.database.TaskDatabaseService
import tk.hiddenname.smarthome.service.task.TaskManager
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/tasks"])
class TaskRestController(private val service: TaskDatabaseService,
                         private val manager: TaskManager) {

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun getAll(): List<Task> = service.getAll()

    @PostMapping(value = ["/create"], produces = ["application/json"])
    @Throws(NoSuchProcessorException::class, UnsupportedTriggerObjectTypeException::class, NoSuchListenerException::class,
            TriggerExistsException::class, ProcessorExistsException::class, UnsupportedProcessingObjectTypeException::class)
    fun create(@RequestBody(required = true) task: @Valid Task): Task {
        var newTask = task
        newTask = service.create(newTask)
        manager.addTask(newTask)

        return newTask
    }

    @DeleteMapping(value = ["/one/delete/{id}"], produces = ["application/json"])
    fun delete(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Any> {
        manager.removeTask(id)
        service.delete(id)

        return ResponseEntity.noContent().build()
    }
}