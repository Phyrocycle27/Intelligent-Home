package tk.hiddenname.smarthome.controller.util;

import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(GpioHelperRestController.class);

    private final GpioManager gpioManager;

    @GetMapping(value = {"/available"}, produces = {"application/json"})
    public String getAvailableGpioPins(@RequestParam(name = "type") String t) {
        try {
            SignalType type = SignalType.valueOf(t.toUpperCase());
            return getJsonWithAvailableGpioPins(type);
        } catch (IllegalArgumentException e) {
            SignalTypeNotFoundException ex = new SignalTypeNotFoundException(t);
            log.warn(ex.getMessage());
            throw ex;
        }
    }

    private String getJsonWithAvailableGpioPins(SignalType type) {
        JSONObject obj = new JSONObject();

        switch (type) {
            case DIGITAL:
                return new JSONObject().put("available_gpio_pins",
                        new JSONArray(gpioManager.getAvailableDigitalGPIO())).toString();
            case PWM:
                return new JSONObject().put("available_gpio_pins",
                        new JSONArray(gpioManager.getAvailablePwmGPIO())).toString();
            default:
                return null;
        }
    }
}
