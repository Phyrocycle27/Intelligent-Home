package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class TaskNotFoundException(id: Long) : Exception("The task with id '$id' not found")