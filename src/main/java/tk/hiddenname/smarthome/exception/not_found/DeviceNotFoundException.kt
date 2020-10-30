package tk.hiddenname.smarthome.exception.not_found

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class DeviceNotFoundException(id: Long) : RuntimeException("The device with id '$id' not found")