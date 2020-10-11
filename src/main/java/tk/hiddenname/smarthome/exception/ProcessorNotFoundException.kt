package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ProcessorNotFoundException(id: Int) : Exception("The processor with id '$id' not found")