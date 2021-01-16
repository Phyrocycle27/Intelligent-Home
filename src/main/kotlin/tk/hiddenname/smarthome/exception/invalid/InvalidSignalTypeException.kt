package tk.hiddenname.smarthome.exception.invalid

import tk.hiddenname.smarthome.exception.ApiException
import tk.hiddenname.smarthome.model.signal.SignalType

class InvalidSignalTypeException(type: String, availableTypes: Array<SignalType>) :
    ApiException("The type '$type' is invalid! Available types are: ${availableTypes.joinToString(", ")}")