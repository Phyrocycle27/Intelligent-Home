package tk.hiddenname.smarthome.model.signal

import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException

enum class SignalType {
    DIGITAL,
    PWM,
    ANALOG,
    NOT_SPECIFIED
}

fun getSignalType(signalTypeName: String): SignalType {
    try {
        return SignalType.valueOf(signalTypeName.toUpperCase())
    } catch (e: IllegalArgumentException) {
        throw SignalTypeNotFoundException(signalTypeName)
    }
}