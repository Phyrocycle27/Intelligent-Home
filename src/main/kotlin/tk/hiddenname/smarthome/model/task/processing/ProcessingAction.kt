package tk.hiddenname.smarthome.model.task.processing

import com.fasterxml.jackson.annotation.JsonCreator
import org.slf4j.LoggerFactory
import tk.hiddenname.smarthome.exception.invalid.InvalidProcessingActionException

enum class ProcessingAction {
    SET_PWM_SIGNAL,
    SET_DIGITAL_SIGNAL;

    companion object {

        private val log = LoggerFactory.getLogger(ProcessingAction::class.java)

        @JvmStatic
        @JsonCreator
        @Throws(InvalidProcessingActionException::class)
        fun getProcessingAction(processingActionName: String): ProcessingAction {
            try {
                return valueOf(processingActionName.toUpperCase())
            } catch (e: IllegalArgumentException) {
                log.error(e.message)
                throw InvalidProcessingActionException(processingActionName, values())
            }
        }
    }
}