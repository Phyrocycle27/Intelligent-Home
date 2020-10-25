package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

// TODO: Обозначить код ошибки HTTP
@ResponseStatus(value = HttpStatus.NOT_FOUND)
class GpioNotFoundException(gpioPin: Int) : Exception("The gpio pin with number '$gpioPin' not found")