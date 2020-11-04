package tk.hiddenname.smarthome.controller.sensor

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.service.database.SensorDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.digital.input.DigitalSensorService
import javax.validation.constraints.Min

@Validated
@RestController
@RequestMapping(value = ["/sensors/reading"])
class SensorSignalRestController(private val dbService: SensorDatabaseService,
                                 private val digitalService: DigitalSensorService) {

    @GetMapping(value = ["/digital/{id}"], produces = ["application/json"])
    fun getState(@Min(1) @PathVariable(name = "id", required = true) id: Long): DigitalState {
        val sensor = dbService.getOne(id)
        return digitalService.getState(sensor.id, sensor.signalInversion)
    }
}