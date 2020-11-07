package tk.hiddenname.smarthome.exception.invalid

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import tk.hiddenname.smarthome.model.task.trigger.TriggerAction

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class InvalidTriggerActionException(triggerActionName: String, availableTypes: Array<TriggerAction>) :
        RuntimeException("The trigger action '$triggerActionName' is invalid! " +
                "Available types are: ${availableTypes.joinToString(", ")}")