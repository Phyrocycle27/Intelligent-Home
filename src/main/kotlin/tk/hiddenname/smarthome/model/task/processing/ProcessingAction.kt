package tk.hiddenname.smarthome.model.task.processing

import com.fasterxml.jackson.annotation.JsonCreator
import tk.hiddenname.smarthome.exception.invalid.InvalidProcessingActionException

enum class ProcessingAction {
    SET_PWM_SIGNAL,
    SET_DIGITAL_SIGNAL;

    companion object {
        @JvmStatic
        @JsonCreator
        @Throws(InvalidProcessingActionException::class)
        fun getProcessingAction(processingActionName: String): ProcessingAction {
            try {
                return valueOf(processingActionName.toUpperCase())
            } catch (e: IllegalArgumentException) {
                throw InvalidProcessingActionException(processingActionName)
            }
        }
    }
}