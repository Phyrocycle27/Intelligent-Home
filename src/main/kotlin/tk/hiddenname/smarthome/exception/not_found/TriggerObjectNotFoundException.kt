package tk.hiddenname.smarthome.exception.not_found

import tk.hiddenname.smarthome.exception.ApiException

class TriggerObjectNotFoundException(id: Long) : ApiException("The trigger object with id '$id' not found")