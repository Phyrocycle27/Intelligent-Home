package tk.hiddenname.smarthome.exception.not_specified

import tk.hiddenname.smarthome.exception.ApiException

class GpioPinNotSpecifiedException : ApiException("The gpio pin is not specified")