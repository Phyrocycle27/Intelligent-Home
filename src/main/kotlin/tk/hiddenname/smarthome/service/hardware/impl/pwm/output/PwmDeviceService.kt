package tk.hiddenname.smarthome.service.hardware.impl.pwm.output

import tk.hiddenname.smarthome.model.signal.PwmSignal
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService

interface PwmDeviceService : GPIOService {

    fun getSignal(id: Long, reverse: Boolean): PwmSignal

    fun setSignal(id: Long, reverse: Boolean, newSignal: Int): PwmSignal
}