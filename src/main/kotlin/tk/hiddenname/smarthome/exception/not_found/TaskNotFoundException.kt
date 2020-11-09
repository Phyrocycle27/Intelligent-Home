package tk.hiddenname.smarthome.exception.not_found

import tk.hiddenname.smarthome.exception.ApiException

class TaskNotFoundException(id: Long) : ApiException("The task with id '$id' not found")