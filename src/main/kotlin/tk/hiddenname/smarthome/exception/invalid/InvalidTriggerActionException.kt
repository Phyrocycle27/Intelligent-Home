package tk.hiddenname.smarthome.exception.invalid

import tk.hiddenname.smarthome.exception.ApiException
import tk.hiddenname.smarthome.model.task.trigger.TriggerAction

class InvalidTriggerActionException(triggerActionName: String, availableTypes: Array<TriggerAction>) :
        ApiException("The trigger action '$triggerActionName' is invalid! " +
                "Available types are: ${availableTypes.joinToString(", ")}")