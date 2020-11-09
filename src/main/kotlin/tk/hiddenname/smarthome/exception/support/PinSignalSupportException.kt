package tk.hiddenname.smarthome.exception.support

import tk.hiddenname.smarthome.exception.ApiException

class PinSignalSupportException(gpioPin: Int) : ApiException("GPIO pin '$gpioPin' doesn't support this signal type")