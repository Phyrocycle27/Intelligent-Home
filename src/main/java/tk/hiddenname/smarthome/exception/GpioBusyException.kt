package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class GpioBusyException(gpioPin: Int) : Exception("The GPIO pin '$gpioPin' has been already busied")