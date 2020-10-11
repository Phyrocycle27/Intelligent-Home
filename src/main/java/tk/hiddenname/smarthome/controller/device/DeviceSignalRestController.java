package tk.hiddenname.smarthome.controller.device;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.model.hardware.Device;
import tk.hiddenname.smarthome.model.signal.DigitalState;
import tk.hiddenname.smarthome.model.signal.PwmSignal;
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService;
import tk.hiddenname.smarthome.service.hardware.impl.digital.output.DigitalDeviceServiceImpl;
import tk.hiddenname.smarthome.service.hardware.impl.pwm.output.PwmDeviceServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping(value = {"/devices/control"})
@AllArgsConstructor
public class DeviceSignalRestController {

    private final DeviceDatabaseService dbService;
    // services
    private final DigitalDeviceServiceImpl digitalService;
    private final PwmDeviceServiceImpl pwmService;

    // ******************************** PWM *************************************************
    @GetMapping(value = {"/pwm"}, produces = {"application/json"})
    public PwmSignal getPwmSignal(@RequestParam(name = "id") Long id) {
        Device device = dbService.getOne(id);

        return pwmService.getSignal(device.getId(), device.isReverse());
    }

    @PutMapping(value = {"/pwm"}, produces = {"application/json"})
    public PwmSignal setPwmSignal(@Valid @RequestBody PwmSignal signal) {
        Device device = dbService.getOne(signal.getId());

        return pwmService.setSignal(device.getId(),
                device.isReverse(),
                signal.getPwmSignal());
    }

    // ***************************** DIGITAL **************************************************

    @GetMapping(value = {"/digital"}, produces = {"application/json"})
    public DigitalState getState(@RequestParam(name = "id") Long id) {
        Device device = dbService.getOne(id);

        return digitalService.getState(device.getId(), device.isReverse());
    }

    @PutMapping(value = {"/digital"}, produces = {"application/json"})
    public DigitalState setState(@Valid @RequestBody DigitalState state) {
        Device device = dbService.getOne(state.getId());

        return digitalService.setState(device.getId(),
                device.isReverse(),
                state.isDigitalState()
        );
    }
}
