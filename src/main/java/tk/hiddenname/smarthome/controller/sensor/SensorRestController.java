package tk.hiddenname.smarthome.controller.sensor;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.hardware.Sensor;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SensorNotFoundException;
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;
import tk.hiddenname.smarthome.repository.SensorRepository;
import tk.hiddenname.smarthome.service.hardware.manager.SensorManager;
import tk.hiddenname.smarthome.utils.gpio.GPIOManager;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = {"/sensors"})
@AllArgsConstructor
public class SensorRestController {

    private static final Logger log = LoggerFactory.getLogger(SensorRestController.class);

    private final SensorRepository sensorRepo;
    // Gpio creator
    private final SensorManager manager;

    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Sensor> getAll()
            throws SignalTypeNotFoundException {
        return sensorRepo.findAll(Sort.by("id"));
    }

    @GetMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Sensor getOne(@PathVariable Integer id) {
        return sensorRepo.findById(id).orElseThrow(() -> {
            SensorNotFoundException e = new SensorNotFoundException(id);
            log.warn(e.getMessage());
            return e;
        });
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Sensor create(@Valid @RequestBody Sensor newSensor) throws GPIOBusyException, PinSignalSupportException,
            SignalTypeNotFoundException {

        GPIOManager.validate(newSensor.getGpio().getGpio(), newSensor.getGpio().getType());
        newSensor.setCreationDate(LocalDateTime.now());

        newSensor = sensorRepo.save(newSensor);
        manager.create(newSensor);

        return newSensor;
    }

    @PutMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Sensor update(@Valid @RequestBody Sensor newSensor, @PathVariable Integer id) {
        return sensorRepo.findById(id)
                .map(sensor -> {
                    BeanUtils.copyProperties(newSensor, sensor, "id", "creationDate", "gpio");
                    return sensorRepo.save(sensor);
                }).orElseThrow(() -> {
                    SensorNotFoundException e = new SensorNotFoundException(id);
                    log.warn(e.getMessage());
                    return e;
                });
    }

    @DeleteMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        manager.delete(sensorRepo.findById(id)
                .orElseThrow(() -> {
                    SensorNotFoundException e = new SensorNotFoundException(id);
                    log.warn(e.getMessage());
                    return e;
                }));

        sensorRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
