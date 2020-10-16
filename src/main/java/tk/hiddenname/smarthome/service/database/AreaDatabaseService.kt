package tk.hiddenname.smarthome.service.database

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.model.Area
import tk.hiddenname.smarthome.repository.AreaRepository

@Service
class AreaDatabaseService(private val repo: AreaRepository) {

    fun getAll(): List<Area> = repo.findAll(Sort.by("id"))

    fun getOne(id: Long): Area = repo.getOne(id)

    fun create(newArea: Area): Area = repo.save(newArea)

    fun delete(id: Long) = repo.deleteById(id)
}