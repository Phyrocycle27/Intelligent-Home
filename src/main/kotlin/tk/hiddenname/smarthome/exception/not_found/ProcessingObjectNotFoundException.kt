package tk.hiddenname.smarthome.exception.not_found

import tk.hiddenname.smarthome.exception.ApiException

class ProcessingObjectNotFoundException(id: Long) : ApiException("The processing object with id '$id' not found")