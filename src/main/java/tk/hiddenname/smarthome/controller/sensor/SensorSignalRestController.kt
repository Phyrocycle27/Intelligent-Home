package tk.hiddenname.smarthome.controller.sensor

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.service.database.SensorDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.digital.input.DigitalSensorService
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/sensors/reading"])
class SensorSignalRestController(private val dbService: SensorDatabaseService,
                                 private val digitalService: DigitalSensorService) {

    @GetMapping(value = ["/digital"], produces = ["application/json"])
    fun getState(@RequestParam(name = "id") id: @Valid Long): DigitalState {
        val sensor = dbService.getOne(id)
        return digitalService.getState(sensor.id, sensor.reverse)
    }
}