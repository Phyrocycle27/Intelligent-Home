package tk.hiddenname.smarthome.controller.sensor

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.exception.GPIOBusyException
import tk.hiddenname.smarthome.exception.PinSignalSupportException
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException
import tk.hiddenname.smarthome.model.hardware.Sensor
import tk.hiddenname.smarthome.model.signal.getSignalType
import tk.hiddenname.smarthome.service.database.SensorDatabaseService
import tk.hiddenname.smarthome.service.hardware.manager.SensorManager
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/sensors"])
class SensorRestController(private val dbService: SensorDatabaseService, private val manager: SensorManager) {

    @Suppress("DuplicatedCode")
    @GetMapping(value = ["/all"], produces = ["application/json"])
    @Throws(SignalTypeNotFoundException::class)
    fun getAll(@RequestParam(name = "type", defaultValue = "", required = false) t: String,
               @RequestParam(name = "areaId", defaultValue = "-1", required = false) areaId: Long): List<Sensor> {

        return if (t.isEmpty() && areaId == -1L) {
            dbService.getAll()
        } else if (t.isNotEmpty()) {
            val type = getSignalType(t)
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
    @Throws(GPIOBusyException::class, PinSignalSupportException::class, SignalTypeNotFoundException::class)
    fun create(@RequestBody(required = true) sensor: @Valid Sensor): Sensor {
        var newSensor = sensor

        newSensor.creationTimestamp = LocalDateTime.now()
        newSensor = dbService.create(newSensor)
        manager.create(newSensor)

        return newSensor
    }

    @PutMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun update(@RequestBody(required = true) newSensor: @Valid Sensor, @PathVariable id: Long): Sensor {
        return dbService.update(id, newSensor)
    }

    @DeleteMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun delete(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Any> {
        val sensor = dbService.getOne(id)

        manager.delete(sensor)
        dbService.delete(id)

        return ResponseEntity.noContent().build()
    }
}