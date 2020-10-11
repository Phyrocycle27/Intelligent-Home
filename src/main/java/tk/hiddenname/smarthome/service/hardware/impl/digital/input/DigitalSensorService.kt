package tk.hiddenname.smarthome.service.hardware.impl.digital.input

import com.pi4j.io.gpio.event.GpioPinListenerDigital
import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService
import tk.hiddenname.smarthome.service.task.impl.listener.Listener
import javax.validation.constraints.NotNull

interface DigitalSensorService : GPIOService {

    fun getState(id: @NotNull Long, reverse: @NotNull Boolean): DigitalState

    fun addListener(listener: @NotNull Listener, sensorId: @NotNull Long, targetSignal: @NotNull Boolean,
                    reverse: @NotNull Boolean): GpioPinListenerDigital

    fun removeListener(listener: GpioPinListenerDigital, sensorId: @NotNull Long)
}