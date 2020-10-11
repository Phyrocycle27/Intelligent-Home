package tk.hiddenname.smarthome.controller.device;

import lombok.AllArgsConstructor;
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

    private final DeviceDatabaseService dbService;
    // Gpio creator
    private final DeviceManager manager;

    @SuppressWarnings("DuplicatedCode")
    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Device> getAll(@RequestParam(name = "type", defaultValue = "", required = false) String t,
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