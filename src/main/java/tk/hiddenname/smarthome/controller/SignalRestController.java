package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.hardware.Sensor;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.SensorNotFoundException;
import tk.hiddenname.smarthome.repository.DeviceRepository;
import tk.hiddenname.smarthome.repository.SensorRepository;
import tk.hiddenname.smarthome.service.hardware.digital.input.DigitalSensorServiceImpl;
import tk.hiddenname.smarthome.service.hardware.digital.output.DigitalDeviceServiceImpl;
import tk.hiddenname.smarthome.service.hardware.pwm.PwmDeviceServiceImpl;

@RestController
@RequestMapping(value = {"/signal"})
@AllArgsConstructor
public class SignalRestController {

    private final SensorRepository sensorRepo;
    private final DeviceRepository deviceRepo;
    // services
    private final DigitalSensorServiceImpl digitalSensorService;
    private final DigitalDeviceServiceImpl digitalDeviceService;
    private final PwmDeviceServiceImpl pwmService;

    // ******************************** PWM *************************************************
    @GetMapping(value = {"/device/pwm"}, produces = {"application/json"})
    public PwmSignal getDevicePwmSignal(@RequestParam(name = "id") Integer id) {
        Device device = deviceRepo.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));

        return pwmService.getSignal(device.getId(), device.getReverse());
    }

    @PutMapping(value = {"/device/pwm"}, produces = {"application/json"})
    public PwmSignal setDevicePwmSignal(@RequestBody PwmSignal signal) {
        Device device = deviceRepo.findById(signal.getId())
                .orElseThrow(() -> new DeviceNotFoundException(signal.getId()));

        return pwmService.setSignal(device.getId(),
                device.getReverse(),
                signal.getPwmSignal());
    }

    // ***************************** DIGITAL **************************************************

    @GetMapping(value = {"/device/digital"}, produces = {"application/json"})
    public DigitalState getDeviceState(@RequestParam(name = "id") Integer id) {
        Device device = deviceRepo.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));

        return digitalDeviceService.getState(device.getId(), device.getReverse());
    }

    @PutMapping(value = {"/device/digital"}, produces = {"application/json"})
    public DigitalState setDeviceState(@RequestBody DigitalState state) {
        Device device = deviceRepo.findById(state.getId())
                .orElseThrow(() -> new DeviceNotFoundException(state.getId()));

        return digitalDeviceService.setState(device.getId(),
                device.getReverse(),
                state.getDigitalState()
        );
    }

    @GetMapping(value = {"/sensor/digital"}, produces = {"application/json"})
    public DigitalState getSensorState(@RequestParam(name = "id") Integer id) {
        Sensor sensor = sensorRepo.findById(id).orElseThrow(() -> new SensorNotFoundException(id));

        return digitalSensorService.getState(sensor.getId(), sensor.getReverse());
    }
}
