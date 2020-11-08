package tk.hiddenname.smarthome.controller.sensor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.service.database.SensorDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.digital.input.DigitalSensorService

@RestController
@RequestMapping(value = ["/sensors/reading"])
open class SensorSignalRestController {

    @Autowired
    open lateinit var dbService: SensorDatabaseService

    @Autowired
    open lateinit var digitalService: DigitalSensorService

    @GetMapping(value = ["/digital/{id}"], produces = ["application/json"])
    fun getState(@PathVariable(name = "id", required = true) id: Long): DigitalState {
        val sensor = dbService.getOne(id)
        return digitalService.getState(sensor.id, sensor.signalInversion)
    }
}