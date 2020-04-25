package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.Device;
import tk.hiddenname.smarthome.entity.GPIO;
import tk.hiddenname.smarthome.entity.GPIOType;
import tk.hiddenname.smarthome.entity.Output;
import tk.hiddenname.smarthome.exception.DeviceAlreadyExistException;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.TypeNotFoundException;
import tk.hiddenname.smarthome.repository.DeviceRepository;
import tk.hiddenname.smarthome.service.DeviceManager;
import tk.hiddenname.smarthome.utils.gpio.GPIOManager;

import java.util.List;

@RestController
@RequestMapping(value = {"/devices"})
@AllArgsConstructor
public class DeviceRestController {

    private static final Logger log = LoggerFactory.getLogger(DeviceRestController.class);

    private final DeviceRepository deviceRepo;
    // Gpio creator
    private final DeviceManager manager;

    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Device> getAll(@RequestParam(name = "type", defaultValue = "", required = false) String t)
            throws TypeNotFoundException {
        log.info("************** GET method: /devices/all?type=" + t + "************************");

        List<Device> devices;

        try {
            GPIOType type = GPIOType.valueOf(t.toUpperCase());
            devices = deviceRepo.findByGpioType(type);
        } catch (IllegalArgumentException e) {
            if (t.isEmpty()) {
                devices = deviceRepo.findAll(Sort.by("id"));
            } else {
                TypeNotFoundException ex = new TypeNotFoundException(t);
                log.warn(ex.getMessage());
                throw ex;
            }
        }

        log.info("Device list is " + devices);
        return devices;

        /* *********************** CONVERTING **************** */
        /*List<Output> outputs = new ArrayList<>();

        for (Device device : devices) {
            GPIO gpio = device.getGpio();
            outputs.add(new Output(
                    device.getId(),
                    device.getName(),
                    gpio.getGpio(),
                    device.getReverse(),
                    device.getCreationDate(),
                    gpio.getType().toString().toLowerCase()
            ));
        }

        log.info("Output list is " + outputs);

        return outputs;*/
    }

    @GetMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Device getOne(@PathVariable Integer id) {
        log.info("************** GET method: /devices/one/" + id + "************************");

        Device device = deviceRepo.findById(id).orElseThrow(() -> {
            DeviceNotFoundException e = new DeviceNotFoundException(id);
            log.warn(e.getMessage());
            return e;
        });

        log.info("Device is " + device);

        return device;
        /* *********************** CONVERTING **************** */
        /*GPIO gpio = device.getGpio();
        Output output = new Output(device.getId(),
                device.getName(),
                gpio.getGpio(),
                device.getReverse(),
                device.getCreationDate(),
                gpio.getType().toString().toLowerCase());

        log.info("Output is " + output);

        return output;*/
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Device create(@RequestBody Device newDevice) throws DeviceAlreadyExistException,
            PinSignalSupportException, TypeNotFoundException {
        log.info("************** POST method: /devices/create ************************");
        log.info("Creating device is " + newDevice);
        /* *********************** CONVERTING **************** */
            /*GPIO gpio = new GPIO(newOutput.getGpio(),
                    GPIOType.valueOf(newOutput.getType().toUpperCase()),
                    GPIOMode.OUTPUT);

            Device device = Device.builder().name(newOutput.getName())
                    .reverse(newOutput.getReverse())
                    .creationDate(LocalDateTime.now())
                    .gpio(gpio).build();

            log.info("Creating device is " + device);*/
        /* *************************************************** */

        GPIOManager.validate(newDevice.getGpio().getGpio(), newDevice.getGpio().getType());

        newDevice = deviceRepo.save(newDevice);
        manager.create(newDevice);

        log.info("Saved device is " + newDevice);

        return newDevice;
        /* *********************** CONVERTING **************** */
            /*newOutput.setCreationDate(device.getCreationDate());
            newOutput.setOutputId(device.getId());

            log.info("Saved output is " + newOutput);

            return device;
            return newOutput;*/
    }

    @PutMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Device update(@RequestBody Device newDevice, @PathVariable Integer id) {
        log.info("************** PUT method: /devices/one/" + id + " ************************");
        log.info("Updating device is " + newDevice);
        /* *********************** CONVERTING **************** */

        /*Device newDevice = Device.builder().name(newOutput.getName())
                .reverse(newOutput.getReverse()).build();

        log.info("Updating device is " + newDevice);*/
        /* *************************************************** */

        Device updated = deviceRepo.findById(id)
                .map(device -> {
                    if (device.getReverse() != newDevice.getReverse()) {
                        manager.update(device);
                    }
                    BeanUtils.copyProperties(newDevice, device, "id", "creationDate", "gpio");
                    return deviceRepo.save(device);
                }).orElseThrow(() -> {
                    DeviceNotFoundException e = new DeviceNotFoundException(id);
                    log.warn(e.getMessage());
                    return e;
                });

        log.info("Saved device is " + updated);
        return updated;
        /* *********************** CONVERTING **************** */
        /*GPIO g = updated.getGpio();
        Output output = new Output(updated.getId(),
                updated.getName(),
                g.getGpio(),
                updated.getReverse(),
                updated.getCreationDate(),
                g.getType().toString().toLowerCase());
        log.info("Saved output is " + output);

        return output;*/
    }

    @DeleteMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        log.info("************** DELETE method: /devices/one/" + id + " ************************");

        manager.delete(deviceRepo.findById(id)
                .orElseThrow(() -> {
                    DeviceNotFoundException e = new DeviceNotFoundException(id);
                    log.warn(e.getMessage());
                    return e;
                }));

        deviceRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = {"/available"}, produces = {"application/json"})
    public String getAvailableOutputs(@RequestParam(name = "type") String t) {
        try {
            GPIOType type = GPIOType.valueOf(t.toUpperCase());

            JSONObject obj = new JSONObject();
            switch (type) {
                case DIGITAL:
                    return obj.put("available_gpios", new JSONArray(GPIOManager.getAvailableDigitalGpios())).toString();
                case PWM:
                    return obj.put("available_gpios", new JSONArray(GPIOManager.getAvailablePwmGpios())).toString();
                default:
                    TypeNotFoundException e = new TypeNotFoundException(t);
                    log.warn(e.getMessage());
                    throw e;
            }
        } catch (IllegalArgumentException e) {
            TypeNotFoundException ex = new TypeNotFoundException(t);
            log.warn(ex.getMessage());
            throw ex;
        }
    }
}