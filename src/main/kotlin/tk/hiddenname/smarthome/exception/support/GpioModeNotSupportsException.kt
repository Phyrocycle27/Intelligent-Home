package tk.hiddenname.smarthome.exception.support

import tk.hiddenname.smarthome.exception.ApiException
import tk.hiddenname.smarthome.model.hardware.GpioMode

class GpioModeNotSupportsException(gpioPin: Int, pinMode: GpioMode) :
        ApiException("GPIO pin '$gpioPin' doesn't support '$pinMode' pin mode")