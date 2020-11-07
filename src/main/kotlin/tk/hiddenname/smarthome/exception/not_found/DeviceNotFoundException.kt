package tk.hiddenname.smarthome.exception.not_found

import tk.hiddenname.smarthome.exception.ApiException

class DeviceNotFoundException(id: Long) : ApiException("The device with id '$id' not found")