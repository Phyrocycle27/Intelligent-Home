package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import tk.hiddenname.smarthome.model.signal.SignalType

@ResponseStatus(value = HttpStatus.CONFLICT)
class UnsupportedSignalTypeException(type: SignalType) :
        RuntimeException("Signal type \"$type\" exists but not supported by your current device's configuration")