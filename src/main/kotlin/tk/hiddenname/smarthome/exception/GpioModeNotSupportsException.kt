package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import tk.hiddenname.smarthome.model.hardware.GpioMode

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class GpioModeNotSupportsException(gpioPin: Int, pinMode: GpioMode) :
        Exception("GPIO pin '$gpioPin' doesn't support '$pinMode' pin mode")
