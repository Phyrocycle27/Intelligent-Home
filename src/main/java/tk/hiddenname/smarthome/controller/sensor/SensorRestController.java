package tk.hiddenname.smarthome.controller.sensor;

import lombok.AllArgsConstructor;
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

    private final SensorDatabaseService dbService;
    // Gpio creator
    private final SensorManager manager;

    @SuppressWarnings("DuplicatedCode")
    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Sensor> getAll(@RequestParam(name = "type", defaultValue = "", required = false) String t,
                               @RequestParam(name = "areaId", defaultValue = "-1", required = false) Integer areaId)
            throws SignalTypeNotFoundException {

        if (t.isEmpty() && areaId == -1) {
            return dbService.getAll();
        } else if (!t.isEmpty() && areaId != -1) {
            SignalType type = SignalType.getSignalType(t);
            return dbService.getAllBySignalTypeAndAreaId(type, areaId);
        } else if (!t.isEmpty()) {
            SignalType type = SignalType.getSignalType(t);
            return dbService.getAllBySignalType(type);
        } else {
            return dbService.getAllByAreaId(areaId);
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
