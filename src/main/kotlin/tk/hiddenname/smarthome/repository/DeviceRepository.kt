package tk.hiddenname.smarthome.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tk.hiddenname.smarthome.model.hardware.Device
import tk.hiddenname.smarthome.model.signal.SignalType
import javax.validation.constraints.NotNull


@Repository
interface DeviceRepository : JpaRepository<Device, Long> {

    fun findAllByGpioSignalType(gpioType: @NotNull SignalType): List<Device>

    fun findAllByAreaId(areaId: @NotNull Long): List<Device>

    fun findAllByGpioSignalTypeAndAreaId(type: @NotNull SignalType, areaId: @NotNull Long): List<Device>

    @Query(value = "SELECT nextval('device_id_seq') + 1", nativeQuery = true)
    fun getNextId(): Long
}