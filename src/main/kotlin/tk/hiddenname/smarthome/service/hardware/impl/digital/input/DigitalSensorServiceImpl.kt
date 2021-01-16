package tk.hiddenname.smarthome.service.hardware.impl.digital.input

import com.pi4j.io.gpio.GpioPinDigitalInput
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent
import com.pi4j.io.gpio.event.GpioPinListenerDigital
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.not_found.SensorNotFoundException
import tk.hiddenname.smarthome.model.hardware.GPIO
import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.service.task.impl.listener.Listener
import tk.hiddenname.smarthome.utils.gpio.GpioManager

@Service
class DigitalSensorServiceImpl(private val gpioManager: GpioManager) : DigitalSensorService {

    @Suppress("unused")
    private val log = LoggerFactory.getLogger(DigitalSensorServiceImpl::class.java)

    private val digitalInputsToSensorId: MutableMap<Long, GpioPinDigitalInput> = HashMap()

    override fun delete(id: Long) {
        val pin = digitalInputsToSensorId[id] ?: throw SensorNotFoundException(id)
        gpioManager.deletePin(pin)
        digitalInputsToSensorId.remove(id)
    }

    override fun save(id: Long, gpio: GPIO, reverse: Boolean) {
        digitalInputsToSensorId[id] = gpioManager.createDigitalInput(gpio)
    }

    override fun update(id: Long, reverse: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getState(id: Long, reverse: Boolean): DigitalState {
        val pin = digitalInputsToSensorId[id] ?: throw SensorNotFoundException(id)
        return DigitalState(id, reverse xor pin.isHigh)
    }

    override fun addListener(
        listener: Listener, sensorId: Long,
        targetSignal: Boolean, reverse: Boolean
    ): GpioPinListenerDigital {
        val pin = digitalInputsToSensorId[sensorId] ?: throw SensorNotFoundException(sensorId)
        return run {
            val pinListener = GpioPinListenerDigital { event: GpioPinDigitalStateChangeEvent ->
                listener.trigger(event.state.isHigh xor reverse == targetSignal)
            }
            pin.addListener(pinListener)
            pinListener
        }
    }

    override fun removeListener(listener: GpioPinListenerDigital, sensorId: Long) {
        digitalInputsToSensorId[sensorId]?.removeListener(listener) ?: throw SensorNotFoundException(sensorId)
    }
}