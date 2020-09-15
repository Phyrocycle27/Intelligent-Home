package tk.hiddenname.smarthome.controller.util;

import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;
import tk.hiddenname.smarthome.utils.gpio.GpioManager;

@RestController
@RequestMapping(value = {"/util/gpio"})
@AllArgsConstructor
public class GpioHelperRestController {

    private final GpioManager gpioManager;

    @GetMapping(value = {"/available"}, produces = {"application/json"})
    public String getAvailableGpioPins(@RequestParam(name = "type") String t) {
        try {
            SignalType type = SignalType.valueOf(t.toUpperCase());
            return getJsonWithAvailableGpioPins(type);
        } catch (IllegalArgumentException e) {
            throw new SignalTypeNotFoundException(t);
        }
    }

    private String getJsonWithAvailableGpioPins(SignalType type) {
        if (type == SignalType.DIGITAL || type == SignalType.PWM) {
            return new JSONObject().put("available_gpio_pins",
                    new JSONArray(gpioManager.getAvailableGpioPins(type))).toString();
        } else {
            return null;
        }
    }
}
