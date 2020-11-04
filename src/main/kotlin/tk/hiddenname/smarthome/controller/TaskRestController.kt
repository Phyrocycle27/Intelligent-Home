package tk.hiddenname.smarthome.controller

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.exception.*
import tk.hiddenname.smarthome.model.task.Task
import tk.hiddenname.smarthome.service.database.TaskDatabaseService
import tk.hiddenname.smarthome.service.task.TaskService
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.Min

@Validated
@RestController
@RequestMapping(value = ["/tasks"])
class TaskRestController(private val dbService: TaskDatabaseService,
                         private val taskService: TaskService) {

    private val log = LoggerFactory.getLogger(TaskRestController::class.java)

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun getAll(): List<Task> = dbService.getAll()

    @GetMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun getOne(@Min(1) @PathVariable(name = "id") id: Long) = dbService.getOne(id)

    @PostMapping(value = ["/create"], produces = ["application/json"])
    @Throws(NoSuchProcessorException::class, UnsupportedTriggerObjectTypeException::class, NoSuchListenerException::class,
            TriggerExistsException::class, ProcessorExistsException::class, UnsupportedProcessingObjectTypeException::class)
    fun create(@Valid @RequestBody(required = true) task: Task): Task {
        task.id = dbService.getNextId()
        task.creationTimestamp = LocalDateTime.now()
        task.updateTimestamp = task.creationTimestamp
        log.info(task.toString())

        //taskService.addTask(task)
        return dbService.create(task)
    }

    @DeleteMapping(value = ["/one/delete/{id}"], produces = ["application/json"])
    fun delete(@Min(1) @PathVariable(name = "id", required = true) id: Long): ResponseEntity<Any> {
        taskService.removeTask(id)
        dbService.delete(id)

        return ResponseEntity.noContent().build()
    }
}