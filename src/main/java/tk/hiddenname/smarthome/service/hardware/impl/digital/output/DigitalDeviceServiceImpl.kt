package tk.hiddenname.smarthome.service.hardware.impl.digital.output

import com.pi4j.io.gpio.GpioPinDigitalOutput
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.DeviceNotFoundException
import tk.hiddenname.smarthome.exception.GPIOBusyException
import tk.hiddenname.smarthome.exception.PinSignalSupportException
import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.utils.gpio.GpioManager
import tk.hiddenname.smarthome.utils.gpio.GpioSignalController

@Service
class DigitalDeviceServiceImpl(private val controller: GpioSignalController,
                               private val gpioManager: GpioManager) : DigitalDeviceService {

    private val digitalOutputsToDeviceId: MutableMap<Long, GpioPinDigitalOutput> = HashMap()

    @Throws(DeviceNotFoundException::class)
    override fun delete(id: Long) {
        val pin = digitalOutputsToDeviceId[id] ?: throw DeviceNotFoundException(id)
        controller.setState(pin, false)
        gpioManager.deletePin(pin)
        digitalOutputsToDeviceId.remove(id)
    }

    @Throws(GPIOBusyException::class, PinSignalSupportException::class)
    override fun save(id: Long, gpioPin: Int, reverse: Boolean) {
        digitalOutputsToDeviceId[id] = gpioManager.createDigitalOutput(gpioPin, reverse)
    }

    @Throws(DeviceNotFoundException::class)
    override fun update(id: Long, reverse: Boolean) {
        setState(id, reverse, getState(id, reverse).isDigitalState)
    }

    @Throws(DeviceNotFoundException::class)
    override fun getState(id: Long, reverse: Boolean): DigitalState {
        return DigitalState(id, reverse xor getPin(id).isHigh)
    }

    @Throws(DeviceNotFoundException::class)
    override fun setState(id: Long, reverse: Boolean, newState: Boolean): DigitalState {
        return DigitalState(id, reverse xor controller.setState(getPin(id), reverse xor newState))
    }

    @Throws(DeviceNotFoundException::class)
    private fun getPin(id: Long): GpioPinDigitalOutput {
        return digitalOutputsToDeviceId[id] ?: throw DeviceNotFoundException(id)
    }
}