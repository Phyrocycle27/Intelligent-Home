package tk.hiddenname.smarthome.exception.exist

import tk.hiddenname.smarthome.exception.ApiException

class GpioPinBusyException(gpioPin: Int) : ApiException("The GPIO pin '$gpioPin' has been already busied")