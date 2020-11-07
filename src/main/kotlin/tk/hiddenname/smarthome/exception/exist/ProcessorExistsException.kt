package tk.hiddenname.smarthome.exception.exist

import tk.hiddenname.smarthome.exception.ApiException

class ProcessorExistsException(id: Long) : ApiException("Processor with id '$id' has been already existed")