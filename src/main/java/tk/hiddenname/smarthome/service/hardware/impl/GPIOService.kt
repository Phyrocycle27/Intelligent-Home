package tk.hiddenname.smarthome.service.hardware.impl

import tk.hiddenname.smarthome.exception.DeviceNotFoundException
import tk.hiddenname.smarthome.exception.GpioBusyException
import tk.hiddenname.smarthome.exception.PinSignalSupportException

interface GPIOService {

    @Throws(DeviceNotFoundException::class)
    fun delete(id: Long)

    @Throws(GpioBusyException::class, PinSignalSupportException::class)
    fun save(id: Long, gpioPin: Int, reverse: Boolean)

    @Throws(DeviceNotFoundException::class)
    fun update(id: Long, reverse: Boolean)
}