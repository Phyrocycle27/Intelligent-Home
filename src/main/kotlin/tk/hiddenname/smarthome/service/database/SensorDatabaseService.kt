package tk.hiddenname.smarthome.service.database

import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.not_found.SensorNotFoundException
import tk.hiddenname.smarthome.model.hardware.Sensor
import tk.hiddenname.smarthome.model.signal.SignalType
import tk.hiddenname.smarthome.repository.SensorRepository

@Service
class SensorDatabaseService(private val repo: SensorRepository) {

    @Suppress("unused")
    private val log = LoggerFactory.getLogger(SensorDatabaseService::class.java)

    fun getAll(): List<Sensor> = repo.findAll(Sort.by("id"))

    fun getAllBySignalType(type: SignalType): List<Sensor> {
        return repo.findAllByGpioSignalType(type)
    }

    fun getAllByAreaId(areaId: Long): List<Sensor> {
        return repo.findAllByAreaId(areaId)
    }

    fun getAllBySignalTypeAndAreaId(type: SignalType, areaId: Long): List<Sensor> {
        return repo.findAllByGpioSignalTypeAndAreaId(type, areaId)
    }

    fun getOne(id: Long): Sensor {
        return repo.findById(id).orElseThrow { SensorNotFoundException(id) }
    }

    fun create(newDevice: Sensor): Sensor = repo.save(newDevice)

    fun update(id: Long, newSensor: Sensor): Sensor {
        return repo.findById(id)
            .map { sensor: Sensor ->
                BeanUtils.copyProperties(newSensor, sensor, "id", "creationTimestamp", "gpio")
                sensor.gpio?.signalType = newSensor.gpio?.signalType
                repo.save(sensor)
            }.orElseThrow { SensorNotFoundException(id) }
    }

    fun getNextId() = repo.getNextId()

    fun delete(id: Long) {
        repo.delete(repo.findById(id).orElseThrow { SensorNotFoundException(id) })
    }
}