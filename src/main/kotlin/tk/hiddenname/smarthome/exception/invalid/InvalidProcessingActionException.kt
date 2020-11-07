package tk.hiddenname.smarthome.exception.invalid

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import tk.hiddenname.smarthome.model.task.processing.ProcessingAction

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class InvalidProcessingActionException(processingActionName: String, availableTypes: Array<ProcessingAction>) :
        RuntimeException("The processing action '$processingActionName' is invalid! " +
                "Available types are: ${availableTypes.joinToString(", ")}")
