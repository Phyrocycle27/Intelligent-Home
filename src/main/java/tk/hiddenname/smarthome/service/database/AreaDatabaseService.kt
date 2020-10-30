package tk.hiddenname.smarthome.service.database

import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.not_found.AreaNotFoundException
import tk.hiddenname.smarthome.model.Area
import tk.hiddenname.smarthome.repository.AreaRepository

@Service
class AreaDatabaseService(private val repo: AreaRepository) {

    fun getAll(): List<Area> = repo.findAll(Sort.by("id"))

    @Throws(AreaNotFoundException::class)
    fun getOne(id: Long): Area {
        return repo.findById(id).orElseThrow { AreaNotFoundException(id) }
    }

    fun create(newArea: Area): Area = repo.save(newArea)

    @Throws(AreaNotFoundException::class)
    fun delete(id: Long) {
        repo.delete(repo.findById(id).orElseThrow { AreaNotFoundException(id) })
    }

    @Throws(AreaNotFoundException::class)
    fun update(id: Long, newArea: Area): Area {
        return repo.findById(id)
                .map { area: Area ->
                    BeanUtils.copyProperties(newArea, area, "id")
                    repo.save(area)
                }.orElseThrow { AreaNotFoundException(id) }
    }
}