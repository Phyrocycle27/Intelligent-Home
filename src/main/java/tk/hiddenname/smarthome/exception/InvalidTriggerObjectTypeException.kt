package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class InvalidTriggerObjectTypeException(id: String)
    : RuntimeException("The type '$id'of trigger object is invalid")