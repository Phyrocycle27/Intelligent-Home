package tk.hiddenname.smarthome.exception.not_specified

import tk.hiddenname.smarthome.exception.ApiException

class TriggerObjectPropertyNotSpecifiedException(propertyName: String) :
    ApiException("The '$propertyName' of trigger object is not specified")