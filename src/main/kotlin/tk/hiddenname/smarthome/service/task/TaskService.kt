package tk.hiddenname.smarthome.service.task

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.not_found.TaskNotFoundException
import tk.hiddenname.smarthome.model.hardware.Device
import tk.hiddenname.smarthome.model.signal.SignalType
import tk.hiddenname.smarthome.model.task.Task
import tk.hiddenname.smarthome.model.task.processing.ProcessingAction
import tk.hiddenname.smarthome.model.task.processing.objects.SetDigitalSignalObject
import tk.hiddenname.smarthome.model.task.processing.objects.SetPwmSignalObject
import tk.hiddenname.smarthome.model.task.trigger.TriggerAction
import tk.hiddenname.smarthome.model.task.trigger.objects.ChangeDigitalSignalObject
import tk.hiddenname.smarthome.service.database.ProcessingObjectDatabaseService
import tk.hiddenname.smarthome.service.database.TaskDatabaseService
import tk.hiddenname.smarthome.service.database.TriggerObjectDatabaseService
import tk.hiddenname.smarthome.service.task.impl.TaskManager

@Service
class TaskService(
    private val context: ApplicationContext,
    private val taskDBService: TaskDatabaseService,
    private val triggerObjectDBService: TriggerObjectDatabaseService,
    private val processingObjectDBService: ProcessingObjectDatabaseService
) {

    private val log = LoggerFactory.getLogger(TaskService::class.java)

    private val taskManagers = HashMap<Long, TaskManager>()

    fun addTask(task: Task): Task {
        val taskManager = context.getBean(TaskManager::class.java)
        taskManagers[task.id] = taskManager.register(task)

        log.info(task.toString())
        return taskDBService.create(task)
    }

    fun removeTask(taskId: Long) {
        taskManagers[taskId]?.unregister() ?: throw TaskNotFoundException(taskId)
        taskManagers.remove(taskId)
        taskDBService.delete(taskId)
    }

    fun loadTasks() {
        taskDBService.getAll().iterator().forEach {
            try {
                addTask(it)
            } catch (e: Exception) {
                log.error(e.message)
            }
        }
    }

    fun removeTriggerBySensorId(sensorId: Long) {
        taskDBService.getAllByTriggerObjectsAction(TriggerAction.CHANGE_DIGITAL_SIGNAL).forEach { task ->
            task.triggerObjects.forEach { triggerObject ->
                if (triggerObject is ChangeDigitalSignalObject && triggerObject.id == sensorId) {
                    taskManagers[task.id]?.unregisterListener(triggerObject.id)
                    //TODO: удалить TriggerObject из базы данных
                }
            }
        }

        taskManagers.forEach{ if (it.value.getListenersCount() == 0) removeTask(it.key) }
    }

    fun removeProcessorByDevice(device: Device) {
        when(device.gpio?.signalType) {
            SignalType.DIGITAL -> taskDBService.getAllByProcessingObjectsAction(ProcessingAction.SET_DIGITAL_SIGNAL)
                .forEach { task ->
                task.processingObjects.forEach { processingObject ->
                    if (taskManagers[task.id] != null && processingObject is SetDigitalSignalObject &&
                        processingObject.id == device.id) {
                        taskManagers[task.id]!!.unregisterProcessor(processingObject.id)
                        //TODO: удалить ProcessingObject из БД

                    }
                }
            }
            SignalType.PWM -> taskDBService.getAllByProcessingObjectsAction (ProcessingAction.SET_PWM_SIGNAL)
                .forEach { task ->
                task.processingObjects.forEach { processingObject ->
                    if (taskManagers[task.id] != null && processingObject is SetPwmSignalObject &&
                        processingObject.id == device.id) {
                        taskManagers[task.id]?.unregisterProcessor(processingObject.id)
                        //TODO: удалить ProcessingObject из БД
                    }
                }
            }
            SignalType.ANALOG -> TODO()
            null -> TODO()
        }

        taskManagers.forEach{ if (it.value.getProcessorsCount() == 0) removeTask(it.key) }
    }
}