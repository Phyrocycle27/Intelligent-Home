package tk.hiddenname.smarthome.service.hardware.impl.pwm.output

import tk.hiddenname.smarthome.exception.DeviceNotFoundException
import tk.hiddenname.smarthome.model.signal.PwmSignal
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService

interface PwmDeviceService : GPIOService {

    @Throws(DeviceNotFoundException::class)
    fun getSignal(id: Long, reverse: Boolean): PwmSignal

    @Throws(DeviceNotFoundException::class)
    fun setSignal(id: Long, reverse: Boolean, newSignal: Int): PwmSignal
}