package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class PinSignalSupportException(gpioPin: Int) : Exception("GPIO pin '$gpioPin' doesn't support this type of signal")