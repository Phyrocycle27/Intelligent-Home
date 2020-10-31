package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

// TODO: Обозначить код ошибки HTTP
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class ProcessingObjectTypeNotSpecifiedException : Exception("Processing object type is not specified")