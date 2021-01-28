package tk.hiddenname.smarthome.service.database

import org.springframework.orm.jpa.JpaSystemException
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.not_found.TriggerObjectNotFoundException
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import tk.hiddenname.smarthome.repository.TriggerObjectRepository

@Service
class TriggerObjectDatabaseService(private val repo: TriggerObjectRepository) {

    fun delete(id: Long)  {
        repo.delete(repo.findById(id).orElseThrow { TriggerObjectNotFoundException(id) })
    }

    fun getAll(): List<TriggerObject> = repo.findAll()

    fun getNextId(): Long {
        return try {
            repo.getNextId()
        } catch (e: JpaSystemException) {
            repo.startIdSequence()
        }
    }
}