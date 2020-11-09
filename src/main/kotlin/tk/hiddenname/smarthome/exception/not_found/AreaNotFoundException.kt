package tk.hiddenname.smarthome.exception.not_found

import tk.hiddenname.smarthome.exception.ApiException

class AreaNotFoundException(id: Long) : ApiException("The area with id '$id' not found")