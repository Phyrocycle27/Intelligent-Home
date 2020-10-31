package tk.hiddenname.smarthome.exception.invalid

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class InvalidSignalTypeException(type: String) : RuntimeException("The type '$type' is invalid!")