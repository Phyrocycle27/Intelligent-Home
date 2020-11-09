package tk.hiddenname.smarthome.exception.support

import tk.hiddenname.smarthome.exception.ApiException

class NoSuchListenerException : ApiException("Listener for the trigger object not found")