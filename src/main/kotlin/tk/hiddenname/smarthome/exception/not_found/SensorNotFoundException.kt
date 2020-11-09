package tk.hiddenname.smarthome.exception.not_found

import tk.hiddenname.smarthome.exception.ApiException

class SensorNotFoundException(id: Long) : ApiException("The sensor with id '$id' not found")