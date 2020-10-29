package tk.hiddenname.smarthome.service.hardware.impl

import tk.hiddenname.smarthome.exception.DeviceNotFoundException
import tk.hiddenname.smarthome.exception.GpioBusyException
import tk.hiddenname.smarthome.exception.PinSignalSupportException
import tk.hiddenname.smarthome.model.hardware.GPIO

interface GPIOService {

    @Throws(DeviceNotFoundException::class)
    fun delete(id: Long)

    @Throws(GpioBusyException::class, PinSignalSupportException::class)
    fun save(id: Long, gpio: GPIO, reverse: Boolean)

    @Throws(DeviceNotFoundException::class)
    fun update(id: Long, reverse: Boolean)
}