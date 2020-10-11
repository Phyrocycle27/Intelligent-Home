package tk.hiddenname.smarthome.service.hardware.impl.digital.output

import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService
import javax.validation.constraints.NotNull

interface DigitalDeviceService : GPIOService {

    fun getState(id: @NotNull Long, reverse: @NotNull Boolean): DigitalState

    fun setState(id: @NotNull Long, reverse: @NotNull Boolean, newState: @NotNull Boolean): DigitalState
}