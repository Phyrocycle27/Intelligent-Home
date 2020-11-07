package tk.hiddenname.smarthome.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.exception.not_found.TaskNotFoundException
import tk.hiddenname.smarthome.model.task.Task
import tk.hiddenname.smarthome.model.task.TaskValidationGroup
import tk.hiddenname.smarthome.service.database.TaskDatabaseService
import tk.hiddenname.smarthome.service.task.TaskService
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.Min

@RestController
@RequestMapping(value = ["/tasks"])
open class TaskRestController {

    @Autowired
    open lateinit var taskService: TaskService

    @Autowired
    open lateinit var dbService: TaskDatabaseService

    companion object {
        @Suppress("unused")
        private val log = LoggerFactory.getLogger(TaskRestController::class.java)
    }

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun getAll(): List<Task> = dbService.getAll()

    @GetMapping(value = ["/one/{id}"], produces = ["application/json"])
    @Throws(TaskNotFoundException::class)
    fun getOne(@Valid @PathVariable(name = "id") @Min(1) id: Long) = dbService.getOne(id)

    @PostMapping(value = ["/create"], produces = ["application/json"])
    fun create(@RequestBody(required = true) @Validated(TaskValidationGroup::class) task: Task): Task {
        task.id = dbService.getNextId()
        task.creationTimestamp = LocalDateTime.now()
        task.updateTimestamp = task.creationTimestamp

        taskService.addTask(task)

        return dbService.create(task)
    }

    @DeleteMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun delete(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Any> {
        taskService.removeTask(id)
        dbService.delete(id)

        return ResponseEntity.noContent().build()
    }
}