package tk.hiddenname.smarthome.controller.util;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.hiddenname.smarthome.entity.hardware.AvailableGpioPins;
import tk.hiddenname.smarthome.entity.signal.SignalTypeKt;
import tk.hiddenname.smarthome.utils.gpio.GpioManager;

@RestController
@RequestMapping(value = {"/util/gpio"})
@AllArgsConstructor
public class GpioHelperRestController {

    private final GpioManager gpioManager;

    @GetMapping(value = {"/available"}, produces = {"application/json"})
    public AvailableGpioPins getAvailableGpioPins(@RequestParam(name = "type") String t) {
        return gpioManager.getAvailableGpioPins(SignalTypeKt.getSignalType(t));
    }
}
