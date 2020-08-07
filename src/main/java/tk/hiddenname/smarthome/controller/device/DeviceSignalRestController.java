package tk.hiddenname.smarthome.controller.device;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.repository.DeviceRepository;
import tk.hiddenname.smarthome.service.hardware.digital.output.DigitalDeviceServiceImpl;
import tk.hiddenname.smarthome.service.hardware.pwm.PwmDeviceServiceImpl;

@RestController
@RequestMapping(value = {"/control"})
@AllArgsConstructor
public class DeviceSignalRestController {

    private final DeviceRepository deviceRepo;
    // services
    private final DigitalDeviceServiceImpl digitalService;
    private final PwmDeviceServiceImpl pwmService;

    // ******************************** PWM *************************************************
    @GetMapping(value = {"/pwm"}, produces = {"application/json"})
    public PwmSignal getPwmSignal(@RequestParam(name = "id") Integer id) {
        Device device = deviceRepo.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));

        return pwmService.getSignal(device.getId(), device.getReverse());
    }

    @PutMapping(value = {"/pwm"}, produces = {"application/json"})
    public PwmSignal setPwmSignal(@RequestBody PwmSignal signal) {
        Device device = deviceRepo.findById(signal.getId())
                .orElseThrow(() -> new DeviceNotFoundException(signal.getId()));

        return pwmService.setSignal(device.getId(),
                device.getReverse(),
                signal.getPwmSignal());
    }

    // ***************************** DIGITAL **************************************************

    @GetMapping(value = {"/digital"}, produces = {"application/json"})
    public DigitalState getState(@RequestParam(name = "id") Integer id) {
        Device device = deviceRepo.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));

        return digitalService.getState(device.getId(), device.getReverse());
    }

    @PutMapping(value = {"/digital"}, produces = {"application/json"})
    public DigitalState setState(@RequestBody DigitalState state) {
        Device device = deviceRepo.findById(state.getId())
                .orElseThrow(() -> new DeviceNotFoundException(state.getId()));

        return digitalService.setState(device.getId(),
                device.getReverse(),
                state.isDigitalState()
        );
    }
}
