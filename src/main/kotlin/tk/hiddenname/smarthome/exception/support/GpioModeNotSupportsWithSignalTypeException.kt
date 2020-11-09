package tk.hiddenname.smarthome.exception.support

import tk.hiddenname.smarthome.exception.ApiException
import tk.hiddenname.smarthome.model.hardware.GpioMode
import tk.hiddenname.smarthome.model.signal.SignalType

class GpioModeNotSupportsWithSignalTypeException(signalType: SignalType, pinMode: GpioMode) :
        ApiException("Gpio mode '$pinMode' not supports with '$signalType' signal type")