package tk.hiddenname.smarthome.exception.not_specified

import tk.hiddenname.smarthome.exception.ApiException

class ProcessingObjectPropertyNotSpecifiedException(propertyName: String) :
    ApiException("The '$propertyName' of processing object is not specified")