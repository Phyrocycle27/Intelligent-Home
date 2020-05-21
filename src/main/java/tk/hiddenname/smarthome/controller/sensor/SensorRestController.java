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
import tk.hiddenname.smarthome.exception.TypeNotFoundException;
import tk.hiddenname.smarthome.repository.SensorRepository;
import tk.hiddenname.smarthome.service.manager.SensorManager;
import tk.hiddenname.smarthome.utils.gpio.GPIOManager;

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
            throws TypeNotFoundException {
        log.info("************** GET method: /sensors/all");
        List<Sensor> sensors;

        sensors = sensorRepo.findAll(Sort.by("id"));

        log.info("Sensor list is " + sensors);
        return sensors;
    }

    @GetMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Sensor getOne(@PathVariable Integer id) {
        log.info("************** GET method: /sensors/one/" + id + "************************");

        Sensor device = sensorRepo.findById(id).orElseThrow(() -> {
            SensorNotFoundException e = new SensorNotFoundException(id);
            log.warn(e.getMessage());
            return e;
        });

        log.info("Sensor is " + device);

        return device;
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Sensor create(@RequestBody Sensor newSensor) throws GPIOBusyException, PinSignalSupportException,
            TypeNotFoundException {
        log.info("************** POST method: /sensors/create ************************");
        log.info("Creating sensor is " + newSensor);

        GPIOManager.validate(newSensor.getGpio().getGpio(), newSensor.getGpio().getType());
        newSensor.setCreationDate(LocalDateTime.now());

        newSensor = sensorRepo.save(newSensor);
        manager.create(newSensor);

        log.info("Saved sensor is " + newSensor);

        return newSensor;
    }

    @PutMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Sensor update(@RequestBody Sensor newSensor, @PathVariable Integer id) {
        log.info("************** PUT method: /sensors/one/" + id + " ************************");
        log.info("Updating sensor is " + newSensor);

        Sensor updated = sensorRepo.findById(id)
                .map(sensor -> {
                    BeanUtils.copyProperties(newSensor, sensor, "id", "creationDate", "gpio");
                    return sensorRepo.save(sensor);
                }).orElseThrow(() -> {
                    SensorNotFoundException e = new SensorNotFoundException(id);
                    log.warn(e.getMessage());
                    return e;
                });

        log.info("Saved sensor is " + updated);
        return updated;
    }

    @DeleteMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        log.info("************** DELETE method: /sensors/one/" + id + " ************************");

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
