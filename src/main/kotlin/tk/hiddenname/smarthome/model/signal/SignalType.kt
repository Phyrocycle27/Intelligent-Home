package tk.hiddenname.smarthome.model.signal

import com.fasterxml.jackson.annotation.JsonCreator
import tk.hiddenname.smarthome.exception.invalid.InvalidSignalTypeException

@Suppress("unused")
enum class SignalType {
    DIGITAL,
    PWM,
    ANALOG;

    companion object {
        @JvmStatic
        @JsonCreator
        fun getSignalType(signalTypeName: String): SignalType {
            try {
                return valueOf(signalTypeName.toUpperCase())
            } catch (e: IllegalArgumentException) {
                throw InvalidSignalTypeException(signalTypeName, values())
            }
        }
    }
}