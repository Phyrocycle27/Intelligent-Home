package tk.hiddenname.smarthome.exception.exist

import tk.hiddenname.smarthome.exception.ApiException

class TriggerExistsException(id: Long) : ApiException("The trigger with id '$id' have been already existed")