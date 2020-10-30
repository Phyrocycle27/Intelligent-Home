package tk.hiddenname.smarthome.exception.not_found

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class TriggerNotFoundException(id: Long) : Exception("The trigger with id '$id' not found")