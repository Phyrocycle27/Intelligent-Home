package tk.hiddenname.smarthome.controller.sensor;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.hiddenname.smarthome.entity.hardware.Sensor;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.exception.SensorNotFoundException;
import tk.hiddenname.smarthome.repository.SensorRepository;
import tk.hiddenname.smarthome.service.digital.input.DigitalSensorServiceImpl;

@RestController
@RequestMapping(value = {"/reading"})
@AllArgsConstructor
public class SensorSignalRestController {

    private final SensorRepository sensorRepo;
    // services
    private final DigitalSensorServiceImpl digitalService;

    // ***************************** DIGITAL **************************************************

    @GetMapping(value = {"/digital"}, produces = {"application/json"})
    public DigitalState getState(@RequestParam(name = "id") Integer id) {
        Sensor sensor = sensorRepo.findById(id).orElseThrow(() -> new SensorNotFoundException(id));

        return digitalService.getState(sensor.getId(), sensor.getReverse());
    }
}
