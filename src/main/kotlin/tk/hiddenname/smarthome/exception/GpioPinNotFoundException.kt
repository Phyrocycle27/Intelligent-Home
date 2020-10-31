package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class GpioPinNotFoundException(gpioPin: Int) : Exception("The gpio pin with number '$gpioPin' not found")