package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
class UnsupportedProcessingObjectTypeException(s: String) :
        Exception("The type ($s) of the processing object not supported by processors")