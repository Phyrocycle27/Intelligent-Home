package tk.hiddenname.smarthome.service.hardware.impl.digital.output

import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService

interface DigitalDeviceService : GPIOService {

    fun getState(id: Long, reverse: Boolean): DigitalState

    fun setState(id: Long, reverse: Boolean, newState: Boolean): DigitalState
}