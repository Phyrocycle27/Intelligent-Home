package tk.hiddenname.smarthome.service.database

import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.not_found.DeviceNotFoundException
import tk.hiddenname.smarthome.model.hardware.Device
import tk.hiddenname.smarthome.model.signal.SignalType
import tk.hiddenname.smarthome.repository.DeviceRepository

@Service
class DeviceDatabaseService(private val repo: DeviceRepository) {

    @Suppress("unused")
    private val log = LoggerFactory.getLogger(DeviceDatabaseService::class.java)

    fun getAll(): List<Device> = repo.findAll(Sort.by("id"))

    fun getAllBySignalType(type: SignalType): List<Device> {
        return repo.findAllByGpioSignalType(type)
    }

    fun getAllByAreaId(areaId: Long): List<Device> {
        return repo.findAllByAreaId(areaId)
    }

    fun getAllBySignalTypeAndAreaId(type: SignalType, areaId: Long): List<Device> {
        return repo.findAllByGpioSignalTypeAndAreaId(type, areaId)
    }

    fun getOne(id: Long): Device {
        return repo.findById(id).orElseThrow { DeviceNotFoundException(id) }
    }

    fun create(newDevice: Device): Device = repo.save(newDevice)

    fun update(id: Long, newDevice: Device): Device {
        return repo.findById(id)
                .map { device: Device ->
                    BeanUtils.copyProperties(newDevice, device, "id", "creationTimestamp", "gpio")
                    device.gpio?.signalType = newDevice.gpio?.signalType
                    repo.save(device)
                }.orElseThrow { DeviceNotFoundException(id) }
    }

    fun getNextId() = repo.getNextId()

    fun delete(id: Long) {
        repo.delete(repo.findById(id).orElseThrow { DeviceNotFoundException(id) })
    }
}