package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
class UnsupportedTriggerObjectTypeException(s: String) :
        Exception("The type ($s) of the trigger object not supported by listeners")