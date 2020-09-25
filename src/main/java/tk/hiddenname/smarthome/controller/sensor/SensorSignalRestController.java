package tk.hiddenname.smarthome.controller.sensor;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.hiddenname.smarthome.entity.hardware.Sensor;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.service.database.SensorDatabaseService;
import tk.hiddenname.smarthome.service.hardware.impl.digital.input.DigitalSensorService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = {"/sensors/reading"})
@AllArgsConstructor
public class SensorSignalRestController {

    private final SensorDatabaseService dbService;
    // services
    private final DigitalSensorService digitalService;

    // ***************************** DIGITAL **************************************************

    @GetMapping(value = {"/digital"}, produces = {"application/json"})
    public DigitalState getState(@Valid @RequestParam(name = "id") Integer id) {
        Sensor sensor = dbService.getOne(id);

        return digitalService.getState(sensor.getId(), sensor.isReverse());
    }
}
