package tk.hiddenname.smarthome.controller.device;

import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;
import tk.hiddenname.smarthome.repository.DeviceRepository;
import tk.hiddenname.smarthome.service.hardware.manager.DeviceManager;
import tk.hiddenname.smarthome.utils.gpio.GPIOManager;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = {"/devices"})
@AllArgsConstructor
public class DeviceRestController {

    private static final Logger log = LoggerFactory.getLogger(DeviceRestController.class);

    private final DeviceRepository repo;
    // Gpio creator
    private final DeviceManager manager;

    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Device> getAll(@RequestParam(name = "type", defaultValue = "", required = false) String t)
            throws SignalTypeNotFoundException {

        List<Device> devices;

        try {
            SignalType type = SignalType.valueOf(t.toUpperCase());
            devices = repo.findByGpioType(type);
        } catch (IllegalArgumentException e) {
            if (t.isEmpty()) {
                devices = repo.findAll(Sort.by("id"));
            } else {
                SignalTypeNotFoundException ex = new SignalTypeNotFoundException(t);
                log.warn(ex.getMessage());
                throw ex;
            }
        }

        return devices;
    }

    @GetMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Device getOne(@PathVariable Integer id) {
        return repo.findById(id).orElseThrow(() -> {
            DeviceNotFoundException e = new DeviceNotFoundException(id);
            log.warn(e.getMessage());
            return e;
        });
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Device create(@Valid @RequestBody Device newDevice) throws GPIOBusyException,
            PinSignalSupportException, SignalTypeNotFoundException {

        GPIOManager.validate(newDevice.getGpio().getGpio(), newDevice.getGpio().getType());
        newDevice.setCreationDate(LocalDateTime.now());

        newDevice = repo.save(newDevice);
        manager.create(newDevice);

        return newDevice;
    }

    @PutMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Device update(@Valid @RequestBody Device newDevice, @PathVariable Integer id) {
        return repo.findById(id)
                .map(device -> {
                    if (device.isReverse() != newDevice.isReverse()) {
                        manager.update(device);
                    }
                    BeanUtils.copyProperties(newDevice, device, "id", "creationDate", "gpio");
                    return repo.save(device);
                }).orElseThrow(() -> {
                    DeviceNotFoundException e = new DeviceNotFoundException(id);
                    log.warn(e.getMessage());
                    return e;
                });
    }

    @DeleteMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        manager.delete(repo.findById(id)
                .orElseThrow(() -> {
                    DeviceNotFoundException e = new DeviceNotFoundException(id);
                    log.warn(e.getMessage());
                    return e;
                }));

        repo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = {"/available"}, produces = {"application/json"})
    public String getAvailableOutputs(@RequestParam(name = "type") String t) {
        try {
            SignalType type = SignalType.valueOf(t.toUpperCase());

            JSONObject obj = new JSONObject();
            switch (type) {
                case DIGITAL:
                    return obj.put("available_gpios", new JSONArray(GPIOManager.getAvailableDigitalGPIO())).toString();
                case PWM:
                    return obj.put("available_gpios", new JSONArray(GPIOManager.getAvailablePwmGPIO())).toString();
                default:
                    SignalTypeNotFoundException e = new SignalTypeNotFoundException(t);
                    log.warn(e.getMessage());
                    throw e;
            }
        } catch (IllegalArgumentException e) {
            SignalTypeNotFoundException ex = new SignalTypeNotFoundException(t);
            log.warn(ex.getMessage());
            throw ex;
        }
    }
}