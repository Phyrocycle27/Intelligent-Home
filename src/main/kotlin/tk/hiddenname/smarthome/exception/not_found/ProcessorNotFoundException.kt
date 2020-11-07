package tk.hiddenname.smarthome.exception.not_found

import tk.hiddenname.smarthome.exception.ApiException

class ProcessorNotFoundException(id: Long) : ApiException("The processor with id '$id' not found")