package tk.hiddenname.smarthome.exception.not_found

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class AreaNotFoundException(id: Long) : RuntimeException("The area with id '$id' not found")