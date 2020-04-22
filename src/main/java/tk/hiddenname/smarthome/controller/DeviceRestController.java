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
import tk.hiddenname.smarthome.entity.*;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;
import tk.hiddenname.smarthome.exception.DeviceAlreadyExistException;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.TypeNotFoundException;
import tk.hiddenname.smarthome.repository.DeviceRepository;
import tk.hiddenname.smarthome.service.DeviceManager;
import tk.hiddenname.smarthome.service.digital.DigitalDeviceServiceImpl;
import tk.hiddenname.smarthome.service.pwm.PwmDeviceServiceImpl;
import tk.hiddenname.smarthome.utils.gpio.GPIOManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = {"/outputs"})
@AllArgsConstructor
public class DeviceRestController {

    private static final Logger log = LoggerFactory.getLogger(DeviceRestController.class);

    private final DeviceRepository deviceRepo;
    // services
    private final DigitalDeviceServiceImpl digitalService;
    private final PwmDeviceServiceImpl pwmService;
    // Gpio creator
    private final DeviceManager manager;

    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Output> getAll(@RequestParam(name = "type", defaultValue = "", required = false) String t)
            throws TypeNotFoundException {
        log.info("************** GET method: /outputs/all?type=" + t + "************************");

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
//        return devices;

        /* *********************** CONVERTING **************** */
        List<Output> outputs = new ArrayList<>();

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

        return outputs;
    }

    @GetMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Output getOne(@PathVariable Integer id) {
        log.info("************** GET method: /outputs/one/" + id + "************************");

        Device device = deviceRepo.findById(id).orElseThrow(() -> {
            DeviceNotFoundException e = new DeviceNotFoundException(id);
            log.warn(e.getMessage());
            return e;
        });

        log.info("Device is " + device);

//        return device;
        /* *********************** CONVERTING **************** */
        GPIO gpio = device.getGpio();
        Output output = new Output(device.getId(),
                device.getName(),
                gpio.getGpio(),
                device.getReverse(),
                device.getCreationDate(),
                gpio.getType().toString().toLowerCase());

        log.info("Output is " + output);

        return output;
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Output create(@RequestBody Output newOutput) throws DeviceAlreadyExistException,
            PinSignalSupportException, TypeNotFoundException {
        log.info("************** POST method: /outputs/create ************************");
        log.info("Creating output is " + newOutput);
        try {
            /* *********************** CONVERTING **************** */
            GPIO gpio = new GPIO(newOutput.getGpio(),
                    GPIOType.valueOf(newOutput.getType().toUpperCase()),
                    GPIOMode.OUTPUT);

            Device device = Device.builder().name(newOutput.getName())
                    .reverse(newOutput.getReverse())
                    .creationDate(LocalDateTime.now())
                    .gpio(gpio).build();

            log.info("Creating device is " + device);
            /* *************************************************** */

            GPIOManager.validate(gpio.getGpio(), gpio.getType());

            device = deviceRepo.save(device);
            manager.create(device);

            log.info("Saved device is " + device);
            /* *********************** CONVERTING **************** */
            newOutput.setCreationDate(device.getCreationDate());
            newOutput.setOutputId(device.getId());

            log.info("Saved output is " + newOutput);

//            return device;
            return newOutput;
        } catch (IllegalArgumentException e) {
            TypeNotFoundException ex = new TypeNotFoundException(newOutput.getType());
            log.warn(ex.getMessage());
            throw ex;
        }
    }

    @PutMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Output update(@RequestBody Output newOutput, @PathVariable Integer id) {
        log.info("************** PUT method: /outputs/one/" + id + " ************************");
        log.info("Updating output is " + newOutput);
        /* *********************** CONVERTING **************** */

        Device newDevice = Device.builder().name(newOutput.getName())
                .reverse(newOutput.getReverse()).build();

        log.info("Updating device is " + newDevice);
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
        /* *********************** CONVERTING **************** */
        GPIO g = updated.getGpio();
        Output output = new Output(updated.getId(),
                updated.getName(),
                g.getGpio(),
                updated.getReverse(),
                updated.getCreationDate(),
                g.getType().toString().toLowerCase());
        log.info("Saved output is " + output);

        return output;
    }

    @DeleteMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        log.info("************** DELETE method: /outputs/one/" + id + " ************************");

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

    /* **************************************************************************************
     ********************************** CONTROL *********************************************
     ************************************************************************************** */

    // ******************************** PWM *************************************************
    @GetMapping(value = {"/control/pwm"}, produces = {"application/json"})
    public PwmSignal getPwmSignal(@RequestParam(name = "id") Integer id) {
        Device device = deviceRepo.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));

        return pwmService.getSignal(device.getGpio().getId(), device.getReverse());
    }

    @PutMapping(value = {"/control/pwm"}, produces = {"application/json"})
    public PwmSignal setPwmSignal(@RequestBody PwmSignal signal) {
        Device device = deviceRepo.findById(signal.getOutputId())
                .orElseThrow(() -> new DeviceNotFoundException(signal.getOutputId()));

        return pwmService.setSignal(device.getGpio().getId(),
                device.getReverse(),
                signal.getPwmSignal());
    }

    // ***************************** DIGITAL **************************************************

    @GetMapping(value = {"/control/digital"}, produces = {"application/json"})
    public DigitalState getState(@RequestParam(name = "id") Integer id) {
        Device device = deviceRepo.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));

        return digitalService.getState(device.getGpio().getId(), device.getReverse());
    }

    @PutMapping(value = {"/control/digital"}, produces = {"application/hal+json"})
    public DigitalState setState(@RequestBody DigitalState state) {
        Device device = deviceRepo.findById(state.getOutputId())
                .orElseThrow(() -> new DeviceNotFoundException(state.getOutputId()));

        return digitalService.setState(device.getGpio().getId(),
                device.getReverse(),
                state.getDigitalState()
        );
    }
}