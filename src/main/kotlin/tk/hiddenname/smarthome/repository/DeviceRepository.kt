package tk.hiddenname.smarthome.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tk.hiddenname.smarthome.model.hardware.Device
import tk.hiddenname.smarthome.model.signal.SignalType


@Repository
interface DeviceRepository : JpaRepository<Device, Long> {

    fun findAllByGpioSignalType(gpioType: SignalType): List<Device>

    fun findAllByAreaId(areaId: Long): List<Device>

    fun findAllByGpioSignalTypeAndAreaId(type: SignalType, areaId: Long): List<Device>

    @Query(value = "SELECT currval('device_id_seq') + 1", nativeQuery = true)
    fun getNextId(): Long

    @Query(value = "SELECT nextval('device_id_seq')", nativeQuery = true)
    fun startIdSequence()
}