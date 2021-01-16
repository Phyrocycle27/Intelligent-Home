package tk.hiddenname.smarthome.service.hardware.impl

import tk.hiddenname.smarthome.exception.exist.GpioPinBusyException
import tk.hiddenname.smarthome.exception.not_found.DeviceNotFoundException
import tk.hiddenname.smarthome.exception.support.PinSignalSupportException
import tk.hiddenname.smarthome.model.hardware.GPIO

interface GPIOService {

    @Throws(DeviceNotFoundException::class)
    fun delete(id: Long)

    @Throws(GpioPinBusyException::class, PinSignalSupportException::class)
    fun save(id: Long, gpio: GPIO, reverse: Boolean)

    @Throws(DeviceNotFoundException::class)
    fun update(id: Long, reverse: Boolean)
}