package tk.hiddenname.smarthome.controller.util

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tk.hiddenname.smarthome.model.hardware.AvailableGpioPins
import tk.hiddenname.smarthome.model.signal.getSignalType
import tk.hiddenname.smarthome.utils.gpio.GpioManager

@RestController
@RequestMapping(value = ["/util/gpio"])
class GpioHelperRestController(private val gpioManager: GpioManager) {

    @GetMapping(value = ["/available"], produces = ["application/json"])
    fun getAvailableGpioPins(@RequestParam(name = "type") t: String): AvailableGpioPins {
        return gpioManager.getAvailableGpioPins(getSignalType(t))
    }
}