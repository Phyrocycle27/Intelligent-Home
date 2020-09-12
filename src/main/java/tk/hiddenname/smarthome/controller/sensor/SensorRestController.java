package tk.hiddenname.smarthome.controller.sensor;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.hardware.Sensor;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;
import tk.hiddenname.smarthome.service.database.SensorDatabaseService;
import tk.hiddenname.smarthome.service.hardware.manager.SensorManager;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = {"/sensors"})
@AllArgsConstructor
public class SensorRestController {

    private static final Logger log = LoggerFactory.getLogger(SensorRestController.class);

    private final SensorDatabaseService dbService;
    // Gpio creator
    private final SensorManager manager;

    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Sensor> getAll(@RequestParam(name = "type", defaultValue = "", required = false) String t)
            throws SignalTypeNotFoundException {
        if (t.isEmpty()) {
            return dbService.getAll();
        } else {
            try {
                SignalType type = SignalType.valueOf(t.toUpperCase());
                return dbService.getAllBySignalType(type);
            } catch (IllegalArgumentException e) {
                SignalTypeNotFoundException ex = new SignalTypeNotFoundException(t);
                log.warn(ex.getMessage());
                throw ex;
            }
        }
    }

    @GetMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Sensor getOne(@PathVariable Integer id) {
        return dbService.getOne(id);
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Sensor create(@Valid @RequestBody Sensor newSensor) throws GPIOBusyException, PinSignalSupportException,
            SignalTypeNotFoundException {

        newSensor.setCreationDate(LocalDateTime.now());
        newSensor = dbService.create(newSensor);
        manager.create(newSensor);

        return newSensor;
    }

    @PutMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Sensor update(@Valid @RequestBody Sensor newSensor, @PathVariable Integer id) {
        return dbService.update(id, newSensor);
    }

    @DeleteMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Sensor sensor = dbService.getOne(id);
        manager.delete(sensor);
        dbService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
