package tk.hiddenname.smarthome.model.task.trigger

import com.fasterxml.jackson.annotation.JsonCreator
import tk.hiddenname.smarthome.exception.invalid.InvalidTriggerActionException

enum class TriggerAction {
    CHANGE_DIGITAL_SIGNAL;

    companion object {
        @JvmStatic
        @JsonCreator
        @Throws(InvalidTriggerActionException::class)
        fun getTriggerAction(triggerActionName: String): TriggerAction {
            try {
                return valueOf(triggerActionName.toUpperCase())
            } catch (e: IllegalArgumentException) {
                throw InvalidTriggerActionException(triggerActionName)
            }
        }
    }
}