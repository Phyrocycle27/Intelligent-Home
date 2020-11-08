package tk.hiddenname.smarthome.controller.sensor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.exception.exist.GpioPinBusyException
import tk.hiddenname.smarthome.exception.invalid.InvalidSignalTypeException
import tk.hiddenname.smarthome.exception.not_specified.GpioNotSpecifiedException
import tk.hiddenname.smarthome.exception.support.PinSignalSupportException
import tk.hiddenname.smarthome.model.hardware.GpioMode
import tk.hiddenname.smarthome.model.hardware.Sensor
import tk.hiddenname.smarthome.model.signal.SignalType
import tk.hiddenname.smarthome.service.database.AreaDatabaseService
import tk.hiddenname.smarthome.service.database.SensorDatabaseService
import tk.hiddenname.smarthome.service.hardware.manager.SensorManager
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/sensors"])
open class SensorRestController {

    @Autowired
    open lateinit var dbService: SensorDatabaseService

    @Autowired
    open lateinit var manager: SensorManager

    @Autowired
    open lateinit var areaDbService: AreaDatabaseService

    @Suppress("DuplicatedCode")
    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun getAll(@RequestParam(name = "signalType", defaultValue = "", required = false) t: String,
               @RequestParam(name = "areaId", defaultValue = "-1", required = false) areaId: Long):
            List<Sensor> {

        return if (t.isEmpty() && areaId == -1L) {
            dbService.getAll()
        } else if (t.isNotEmpty()) {
            val type = SignalType.getSignalType(t)
            if (areaId != -1L) {
                dbService.getAllBySignalTypeAndAreaId(type, areaId)
            } else {
                dbService.getAllBySignalType(type)
            }
        } else {
            dbService.getAllByAreaId(areaId)
        }
    }

    @GetMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun getOne(@PathVariable(name = "id", required = true) id: Long): Sensor = dbService.getOne(id)

    @PostMapping(value = ["/create"], produces = ["application/json"])
    @Throws(GpioPinBusyException::class, PinSignalSupportException::class, InvalidSignalTypeException::class)
    fun create(@Valid @RequestBody(required = true) sensor: Sensor): Sensor {
        initializeSensorFields(sensor)

        manager.register(sensor)
        return dbService.create(sensor)
    }

    @PutMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun update(@Valid @RequestBody(required = true) newSensor: Sensor,
               @PathVariable id: Long): Sensor {
        val oldSensor = dbService.getOne(id)

        if (oldSensor.gpio?.signalType != newSensor.gpio?.signalType) {
            manager.changeSignalType(oldSensor, newSensor.gpio?.signalType)
        }
        if (oldSensor.areaId != newSensor.areaId) {
            areaDbService.getOne(newSensor.areaId)
            oldSensor.areaId = newSensor.areaId
        }

        newSensor.updateTimestamp = LocalDateTime.now()
        return dbService.update(id, newSensor)
    }

    @DeleteMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun delete(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Any> {
        val sensor = dbService.getOne(id)

        manager.unregister(sensor)
        dbService.delete(id)

        return ResponseEntity.noContent().build()
    }

    private fun initializeSensorFields(sensor: Sensor) {
        sensor.creationTimestamp = LocalDateTime.now()
        sensor.updateTimestamp = sensor.creationTimestamp
        sensor.id = dbService.getNextId()

        sensor.gpio ?: throw GpioNotSpecifiedException()
        sensor.gpio.pinMode = GpioMode.INPUT

        if (sensor.areaId != 0L) {
            areaDbService.getOne(sensor.areaId)
        }
    }
}