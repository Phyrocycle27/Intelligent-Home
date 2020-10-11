package tk.hiddenname.smarthome.entity.signal

import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException

enum class SignalType {
    DIGITAL,
    PWM
}

fun getSignalType(signalTypeName: String): SignalType {
    try {
        return SignalType.valueOf(signalTypeName.toUpperCase())
    } catch (e: IllegalArgumentException) {
        throw SignalTypeNotFoundException(signalTypeName)
    }
}