package tk.hiddenname.smarthome.service.hardware.impl.pwm.output

import tk.hiddenname.smarthome.model.signal.PwmSignal
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService
import javax.validation.constraints.NotNull

interface PwmDeviceService : GPIOService {

    fun getSignal(id: @NotNull Long, reverse: @NotNull Boolean): PwmSignal

    fun setSignal(id: @NotNull Long, reverse: @NotNull Boolean, newSignal: @NotNull Int): PwmSignal
}