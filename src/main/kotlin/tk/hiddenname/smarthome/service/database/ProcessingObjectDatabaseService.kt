package tk.hiddenname.smarthome.service.database

import org.springframework.orm.jpa.JpaSystemException
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.not_found.ProcessingObjectNotFoundException
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject
import tk.hiddenname.smarthome.repository.ProcessingObjectRepository

@Service
class ProcessingObjectDatabaseService(private val repo: ProcessingObjectRepository) {

    fun delete(id: Long) {
        repo.delete(repo.findById(id).orElseThrow { ProcessingObjectNotFoundException(id) })
    }

    fun getAll(): List<ProcessingObject> = repo.findAll()

    fun getNextId(): Long {
        return try {
            repo.getNextId()
        } catch (e: JpaSystemException) {
            repo.startIdSequence()
        }
    }
}