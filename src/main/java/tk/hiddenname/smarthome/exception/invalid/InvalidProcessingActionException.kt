package tk.hiddenname.smarthome.exception.invalid

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class InvalidProcessingActionException(processingActionName: String) :
        RuntimeException("The processing action '$processingActionName' is invalid!")
