package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
class SignalTypeNotFoundException(type: String) : RuntimeException("The type '$type' is invalid!")