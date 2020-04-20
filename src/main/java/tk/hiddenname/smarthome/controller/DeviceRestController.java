package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
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

    private final DeviceRepository deviceRepo;
    // services
    private final DigitalDeviceServiceImpl digitalService;
    private final PwmDeviceServiceImpl pwmService;
    // Gpio creator
    private final DeviceManager manager;

    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Output> getAll(
            @RequestParam(name = "type", defaultValue = "", required = false) String t)
            throws TypeNotFoundException {

        List<Device> devices;

        try {
            GPIOType type = GPIOType.valueOf(t.toUpperCase());
            devices = deviceRepo.findByGpioType(type);
        } catch (IllegalArgumentException e) {
            if (t.isEmpty()) {
                devices = deviceRepo.findAll(Sort.by("id"));
            } else {
                throw new TypeNotFoundException(t);
            }
        }

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

        return outputs;
    }

    @GetMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Output getOne(@PathVariable Integer id) {
        Device device = deviceRepo.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));

        /* *********************** CONVERTING **************** */
        GPIO gpio = device.getGpio();
        return new Output(device.getId(),
                device.getName(),
                gpio.getGpio(),
                device.getReverse(),
                device.getCreationDate(),
                gpio.getType().toString().toLowerCase());
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Output create(@RequestBody Output newOutput) throws DeviceAlreadyExistException,
            PinSignalSupportException, TypeNotFoundException {
        try {
            /* *********************** CONVERTING **************** */
            GPIO gpio = new GPIO(newOutput.getGpio(),
                    GPIOType.valueOf(newOutput.getType()),
                    GPIOMode.OUTPUT);

            Device device = new Device(newOutput.getName(),
                    newOutput.getReverse(),
                    LocalDateTime.now(),
                    gpio);
            /* *************************************************** */

            GPIOManager.validate(gpio.getGpio(), gpio.getType());

            device = deviceRepo.save(device);
            manager.create(device);

            /* *********************** CONVERTING **************** */
            newOutput.setCreationDate(device.getCreationDate());
            newOutput.setOutputId(device.getId());

            return newOutput;
        } catch (IllegalArgumentException e) {
            throw new TypeNotFoundException(newOutput.getType());
        }
    }

    @PutMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Output update(@RequestBody Output newOutput, @PathVariable Integer id) {

        /* *********************** CONVERTING **************** */
        GPIO gpio = new GPIO(newOutput.getGpio(),
                GPIOType.valueOf(newOutput.getType()),
                GPIOMode.OUTPUT);

        Device newDevice = new Device(newOutput.getName(),
                newOutput.getReverse(),
                LocalDateTime.now(),
                gpio);
        /* *************************************************** */

        Device updated = deviceRepo.findById(id)
                .map(device -> {
                    if (device.getReverse() != newDevice.getReverse()) {
                        manager.update(device);
                    }

                    BeanUtils.copyProperties(newDevice, device, "id, creationDate, gpio");
                    return deviceRepo.save(device);
                }).orElseThrow(() -> new DeviceNotFoundException(id));


        /* *********************** CONVERTING **************** */
        GPIO g = updated.getGpio();
        return new Output(updated.getId(),
                updated.getName(),
                g.getGpio(),
                updated.getReverse(),
                updated.getCreationDate(),
                g.getType().toString().toLowerCase());
    }

    @DeleteMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        manager.delete(deviceRepo.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id)));

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
                    throw new TypeNotFoundException(t);
            }
        } catch (IllegalArgumentException e) {
            throw new TypeNotFoundException(t);
        }
    }

    /* **************************************************************************************
     ********************************** CONTROL *********************************************
     ************************************************************************************** */

    // ******************************** PWM *************************************************
    @GetMapping(value = {"/control/pwm"}, produces = {"application/json"})
    public PwmSignal getPwmSignal(@RequestParam(name = "id") Integer id) {
        Device device = deviceRepo.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));

        return pwmService.getSignal(id, device.getReverse());
    }

    @PutMapping(value = {"/control/pwm"}, produces = {"application/json"})
    public PwmSignal setPwmSignal(@RequestBody PwmSignal signal) {
        Device device = deviceRepo.findById(signal.getOutputId())
                .orElseThrow(() -> new DeviceNotFoundException(signal.getOutputId()));

        return pwmService.setSignal(
                device.getGpio().getId(),
                device.getReverse(),
                signal.getPwmSignal());
    }

    // ***************************** DIGITAL **************************************************

    @GetMapping(value = {"/control/digital"}, produces = {"application/json"})
    public DigitalState getState(@RequestParam(name = "id") Integer id) {
        Device device = deviceRepo.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));

        return digitalService.getState(id, device.getReverse());
    }

    @PutMapping(value = {"/control/digital"}, produces = {"application/hal+json"})
    public DigitalState setState(@RequestBody DigitalState state) {
        Device device = deviceRepo.findById(state.getOutputId())
                .orElseThrow(() -> new DeviceNotFoundException(state.getOutputId()));

        return digitalService.setState(
                device.getGpio().getId(),
                device.getReverse(),
                state.getDigitalState()
        );
    }
}