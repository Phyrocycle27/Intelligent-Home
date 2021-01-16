package tk.hiddenname.smarthome.service.hardware.impl.digital.input

import com.pi4j.io.gpio.event.GpioPinListenerDigital
import tk.hiddenname.smarthome.exception.not_found.SensorNotFoundException
import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService
import tk.hiddenname.smarthome.service.task.impl.listener.Listener

interface DigitalSensorService : GPIOService {

    @Throws(SensorNotFoundException::class)
    fun getState(id: Long, reverse: Boolean): DigitalState

    @Throws(SensorNotFoundException::class)
    fun addListener(
        listener: Listener, sensorId: Long, targetSignal: Boolean,
        reverse: Boolean
    ): GpioPinListenerDigital

    @Throws(SensorNotFoundException::class)
    fun removeListener(listener: GpioPinListenerDigital, sensorId: Long)
}