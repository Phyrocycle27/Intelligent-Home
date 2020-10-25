package tk.hiddenname.smarthome.controller.device

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.exception.GpioBusyException
import tk.hiddenname.smarthome.exception.PinSignalSupportException
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException
import tk.hiddenname.smarthome.model.hardware.Device
import tk.hiddenname.smarthome.model.signal.getSignalType
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService
import tk.hiddenname.smarthome.service.hardware.manager.DeviceManager
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/devices"])
class DeviceRestController(private val dbService: DeviceDatabaseService,
                           private val manager: DeviceManager) {

    @Suppress("DuplicatedCode")
    @GetMapping(value = ["/all"], produces = ["application/json"])
    @Throws(SignalTypeNotFoundException::class)
    fun getAll(@RequestParam(name = "type", defaultValue = "", required = false) t: String,
               @RequestParam(name = "areaId", defaultValue = "-1", required = false) areaId: Long): List<Device> {

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
    fun getOne(@PathVariable id: Long): Device = dbService.getOne(id)

    @PostMapping(value = ["/create"], produces = ["application/json"])
    @Throws(GpioBusyException::class, PinSignalSupportException::class, SignalTypeNotFoundException::class)
    fun create(@RequestBody(required = true) device: @Valid Device): Device {
        var newDevice = device

        newDevice.creationTimestamp = LocalDateTime.now()
        newDevice = dbService.create(newDevice)
        manager.register(newDevice)

        return newDevice
    }

    @PutMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun update(@RequestBody(required = true) device: @Valid Device, @PathVariable id: Long): Device {
        var newDevice = device
        val oldDevice = dbService.getOne(id)
        newDevice = dbService.update(id, newDevice)

        if (oldDevice.signalInversion != newDevice.signalInversion) {
            manager.update(newDevice)
        }

        return newDevice
    }

    @DeleteMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        val device = dbService.getOne(id)

        manager.unregister(device)
        dbService.delete(id)

        return ResponseEntity.noContent().build()
    }
}