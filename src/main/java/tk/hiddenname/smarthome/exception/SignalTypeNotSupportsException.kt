package tk.hiddenname.smarthome.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import tk.hiddenname.smarthome.model.signal.SignalType

// TODO: Обозначить код ошибки HTTP
@ResponseStatus(value = HttpStatus.NOT_FOUND)
class SignalTypeNotSupportsException(type: SignalType) : RuntimeException("The type '$type' is not supports!")