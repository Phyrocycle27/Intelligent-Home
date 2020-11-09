package tk.hiddenname.smarthome.service.hardware.impl.digital.output

import tk.hiddenname.smarthome.exception.not_found.DeviceNotFoundException
import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService

interface DigitalDeviceService : GPIOService {

    @Throws(DeviceNotFoundException::class)
    fun getState(id: Long, reverse: Boolean): DigitalState

    @Throws(DeviceNotFoundException::class)
    fun setState(id: Long, reverse: Boolean, newState: Boolean): DigitalState
}