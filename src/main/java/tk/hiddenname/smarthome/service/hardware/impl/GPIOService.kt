package tk.hiddenname.smarthome.service.hardware.impl

import tk.hiddenname.smarthome.exception.GPIOBusyException
import tk.hiddenname.smarthome.exception.PinSignalSupportException
import javax.validation.constraints.NotNull

interface GPIOService {

    fun delete(id: @NotNull Long)

    @Throws(GPIOBusyException::class, PinSignalSupportException::class)
    fun save(id: @NotNull Long, gpioPin: @NotNull Int, reverse: @NotNull Boolean)

    fun update(id: @NotNull Long, reverse: @NotNull Boolean)
}