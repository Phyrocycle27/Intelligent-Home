package tk.hiddenname.smarthome.controller.device

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.exception.GpioBusyException
import tk.hiddenname.smarthome.exception.GpioNotSpecifiedException
import tk.hiddenname.smarthome.exception.InvalidSignalTypeException
import tk.hiddenname.smarthome.exception.PinSignalSupportException
import tk.hiddenname.smarthome.model.hardware.Device
import tk.hiddenname.smarthome.model.hardware.GpioMode
import tk.hiddenname.smarthome.model.signal.SignalType
import tk.hiddenname.smarthome.service.database.AreaDatabaseService
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService
import tk.hiddenname.smarthome.service.hardware.manager.DeviceManager
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/devices"])
class DeviceRestController(private val dbService: DeviceDatabaseService,
                           private val areaDbService: AreaDatabaseService,
                           private val manager: DeviceManager) {

    private val log = LoggerFactory.getLogger(DeviceRestController::class.java)

    @Suppress("DuplicatedCode")
    @GetMapping(value = ["/all"], produces = ["application/json"])
    @Throws(InvalidSignalTypeException::class)
    fun getAll(@RequestParam(name = "type", defaultValue = "", required = false) t: String,
               @RequestParam(name = "areaId", defaultValue = "-1", required = false) areaId: Long): List<Device> {

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
    fun getOne(@PathVariable id: Long): Device = dbService.getOne(id)

    @PostMapping(value = ["/create"], produces = ["application/json"])
    @Throws(GpioBusyException::class, PinSignalSupportException::class, InvalidSignalTypeException::class)
    fun create(@Valid @RequestBody(required = true) device: Device): Device {
        initializeDeviceFields(device)

        manager.register(device)
        return dbService.create(device)
    }

    @PutMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun update(@Valid @RequestBody(required = true) newDevice: Device, @PathVariable id: Long): Device {
        val oldDevice = dbService.getOne(id)

        if (oldDevice.gpio?.signalType != newDevice.gpio?.signalType) {
            manager.changeSignalType(oldDevice, newDevice.gpio?.signalType)
        } else if (oldDevice.signalInversion != newDevice.signalInversion) {
            oldDevice.signalInversion = newDevice.signalInversion
            manager.update(oldDevice)
        }

        newDevice.updateTimestamp = LocalDateTime.now()
        return dbService.update(id, newDevice)
    }

    @DeleteMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        val device = dbService.getOne(id)

        manager.unregister(device)
        dbService.delete(id)

        return ResponseEntity.noContent().build()
    }

    private fun initializeDeviceFields(device: Device) {
        device.creationTimestamp = LocalDateTime.now()
        device.updateTimestamp = device.creationTimestamp
        device.id = dbService.getNextId()

        device.gpio ?: throw GpioNotSpecifiedException()
        device.gpio.pinMode = GpioMode.OUTPUT

        if (device.areaId != 0L) {
            areaDbService.getOne(device.areaId)
        }
    }
}