package tk.hiddenname.smarthome.exception.support

import tk.hiddenname.smarthome.exception.ApiException
import tk.hiddenname.smarthome.model.signal.SignalType

class UnsupportedSignalTypeException(type: SignalType) : ApiException("The type '$type' is not supports!")