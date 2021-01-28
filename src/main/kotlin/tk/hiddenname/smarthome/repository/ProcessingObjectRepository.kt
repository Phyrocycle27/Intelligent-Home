package tk.hiddenname.smarthome.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject

@Repository
interface ProcessingObjectRepository : JpaRepository<ProcessingObject, Long> {

    @Query(value = "SELECT currval('processing_object_id_seq') + 1", nativeQuery = true)
    fun getNextId(): Long

    @Query(value = "SELECT nextval('processing_object_id_seq') + 1", nativeQuery = true)
    fun startIdSequence(): Long
}