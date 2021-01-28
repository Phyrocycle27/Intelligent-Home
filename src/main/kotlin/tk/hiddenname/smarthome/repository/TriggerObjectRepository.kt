package tk.hiddenname.smarthome.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject

@Repository
interface TriggerObjectRepository : JpaRepository<TriggerObject, Long> {

    @Query(value = "SELECT currval('trigger_object_id_seq') + 1", nativeQuery = true)
    fun getNextId(): Long

    @Query(value = "SELECT nextval('trigger_object_id_seq')", nativeQuery = true)
    fun startIdSequence(): Long
}