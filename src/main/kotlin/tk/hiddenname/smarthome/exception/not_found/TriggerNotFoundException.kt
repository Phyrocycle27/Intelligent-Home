package tk.hiddenname.smarthome.exception.not_found

import tk.hiddenname.smarthome.exception.ApiException

class TriggerNotFoundException(id: Long) : ApiException("The trigger with id '$id' not found")