package tk.hiddenname.smarthome.exception.not_found

import tk.hiddenname.smarthome.exception.ApiException

class GpioPinNotFoundException(gpioPin: Int) : ApiException("The gpio pin with number '$gpioPin' not found")