package tk.hiddenname.smarthome.service.database

import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.SensorNotFoundException
import tk.hiddenname.smarthome.model.hardware.Sensor
import tk.hiddenname.smarthome.model.signal.SignalType
import tk.hiddenname.smarthome.repository.SensorRepository

@Service
class SensorDatabaseService(private val repo: SensorRepository) {

    fun getAll(): List<Sensor> = repo.findAll(Sort.by("id"))

    fun getAllBySignalType(type: SignalType): List<Sensor> {
        return repo.findAllByGpioType(type)
    }

    fun getAllByAreaId(areaId: Long): List<Sensor> {
        return repo.findAllByAreaId(areaId)
    }

    fun getAllBySignalTypeAndAreaId(type: SignalType, areaId: Long): List<Sensor> {
        return repo.findAllByGpioTypeAndAreaId(type, areaId)
    }

    @Throws(SensorNotFoundException::class)
    fun getOne(id: Long): Sensor {
        return repo.findById(id).orElseThrow { SensorNotFoundException(id) }
    }

    fun create(newDevice: Sensor): Sensor = repo.save(newDevice)

    fun update(id: Long, newSensor: Sensor): Sensor {
        return repo.findById(id)
                .map { sensor: Sensor ->
                    BeanUtils.copyProperties(newSensor, sensor, "id", "creationDate", "gpio")
                    repo.save(sensor)
                }.orElseThrow { SensorNotFoundException(id) }
    }

    fun delete(id: Long) = repo.deleteById(id)
}