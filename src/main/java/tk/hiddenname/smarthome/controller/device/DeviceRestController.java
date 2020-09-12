package tk.hiddenname.smarthome.controller.device;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService;
import tk.hiddenname.smarthome.service.hardware.manager.DeviceManager;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = {"/devices"})
@AllArgsConstructor
public class DeviceRestController {

    private static final Logger log = LoggerFactory.getLogger(DeviceRestController.class);

    private final DeviceDatabaseService dbService;
    // Gpio creator
    private final DeviceManager manager;

    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Device> getAll(@RequestParam(name = "type", defaultValue = "", required = false) String t)
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
    public Device getOne(@PathVariable Integer id) {
        return dbService.getOne(id);
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Device create(@Valid @RequestBody Device newDevice) throws GPIOBusyException,
            PinSignalSupportException, SignalTypeNotFoundException {

        newDevice.setCreationDate(LocalDateTime.now());
        newDevice = dbService.create(newDevice);
        manager.create(newDevice);

        return newDevice;
    }

    @PutMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Device update(@Valid @RequestBody Device newDevice, @PathVariable Integer id) {
        Device device = dbService.getOne(id);
        newDevice = dbService.update(id, newDevice);

        if (device.isReverse() != newDevice.isReverse()) {
            manager.update(newDevice);
        }

        return newDevice;
    }

    @DeleteMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Device device = dbService.getOne(id);
        manager.delete(device);
        dbService.delete(id);

        return ResponseEntity.noContent().build();
    }
}