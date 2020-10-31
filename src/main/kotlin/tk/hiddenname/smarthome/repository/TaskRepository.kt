package tk.hiddenname.smarthome.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tk.hiddenname.smarthome.model.task.Task

@Repository
interface TaskRepository : JpaRepository<Task, Long> {

    @Query(value = "SELECT nextval('task_id_seq') + 1", nativeQuery = true)
    fun getNextId(): Long
}