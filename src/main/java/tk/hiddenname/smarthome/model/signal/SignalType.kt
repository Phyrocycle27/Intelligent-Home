package tk.hiddenname.smarthome.model.signal

import com.fasterxml.jackson.annotation.JsonCreator
import tk.hiddenname.smarthome.exception.InvalidSignalTypeException

enum class SignalType {
    DIGITAL,
    PWM,
    ANALOG,
    NOT_SPECIFIED;

    companion object {
        @JvmStatic
        @JsonCreator
        fun getSignalType(signalTypeName: String): SignalType {
            try {
                return valueOf(signalTypeName.toUpperCase())
            } catch (e: IllegalArgumentException) {
                throw InvalidSignalTypeException(signalTypeName)
            }
        }
    }
}