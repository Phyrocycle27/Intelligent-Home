package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import tk.hiddenname.smarthome.model.hardware.GpioMode
import tk.hiddenname.smarthome.model.signal.SignalType

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class GpioModeNotAvailableWithSignalTypeException(signalType: SignalType, pinMode: GpioMode) :
        Exception("Gpio mode '$pinMode' not available with '$signalType' signal type")