package tk.hiddenname.smarthome.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tk.hiddenname.smarthome.model.hardware.Sensor
import tk.hiddenname.smarthome.model.signal.SignalType
import javax.validation.constraints.NotNull

@Repository
interface SensorRepository : JpaRepository<Sensor, Long> {

    fun findAllByGpioSignalType(type: @NotNull SignalType): List<Sensor>

    fun findAllByAreaId(id: @NotNull Long): List<Sensor>

    fun findAllByGpioSignalTypeAndAreaId(type: @NotNull SignalType, areaId: @NotNull Long): List<Sensor>

    @Query(value = "SELECT nextval('sensor_id_seq') + 1", nativeQuery = true)
    fun getNextId(): Long
}