package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class PinSignalSupportException(gpioPin: Int) : Exception("GPIO pin '$gpioPin' doesn't support this signal type")