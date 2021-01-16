package tk.hiddenname.smarthome.exception.invalid

import tk.hiddenname.smarthome.exception.ApiException
import tk.hiddenname.smarthome.model.task.processing.ProcessingAction

class InvalidProcessingActionException(processingActionName: String, availableTypes: Array<ProcessingAction>) :
    ApiException(
        "The processing action '$processingActionName' is invalid! " +
                "Available types are: ${availableTypes.joinToString(", ")}"
    )
