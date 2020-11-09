package tk.hiddenname.smarthome.exception.not_specified

import tk.hiddenname.smarthome.exception.ApiException

class GpioNotSpecifiedException : ApiException("The gpio object is not specified")