package tk.hiddenname.smarthome.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject

@Repository
interface ProcessingObjectRepository : JpaRepository<ProcessingObject, Long>