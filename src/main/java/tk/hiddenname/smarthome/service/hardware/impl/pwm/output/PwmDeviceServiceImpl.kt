package tk.hiddenname.smarthome.service.hardware.impl.pwm.output

import com.pi4j.io.gpio.GpioPinPwmOutput
import org.springframework.stereotype.Service
import tk.hiddenname.smarthome.exception.DeviceNotFoundException
import tk.hiddenname.smarthome.exception.GpioBusyException
import tk.hiddenname.smarthome.exception.PinSignalSupportException
import tk.hiddenname.smarthome.model.hardware.GPIO
import tk.hiddenname.smarthome.model.signal.PwmSignal
import tk.hiddenname.smarthome.utils.gpio.GpioManager
import tk.hiddenname.smarthome.utils.gpio.GpioSignalController
import java.util.*

@Service
class PwmDeviceServiceImpl(private val controller: GpioSignalController,
                           private val gpioManager: GpioManager) : PwmDeviceService {

    private val map: MutableMap<Long, GpioPinPwmOutput> = HashMap()

    @Throws(DeviceNotFoundException::class)
    override fun delete(id: Long) {
        val pin = map[id] ?: throw DeviceNotFoundException(id)
        controller.setSignal(pin, 0)
        gpioManager.deletePin(pin)
        map.remove(id)
    }

    @Throws(GpioBusyException::class, PinSignalSupportException::class)
    override fun save(id: Long, gpio: GPIO, reverse: Boolean) {
        map[id] = gpioManager.createPwmOutput(gpio, reverse)
    }

    @Throws(DeviceNotFoundException::class)
    override fun update(id: Long, reverse: Boolean) {
        setSignal(id, reverse, getSignal(id, reverse).pwmSignal!!)
    }

    @Throws(DeviceNotFoundException::class)
    override fun getSignal(id: Long, reverse: Boolean): PwmSignal {
        val signal = getPin(id).pwm
        return PwmSignal(id, if (reverse) GpioManager.pwmRange - signal else signal)
    }

    @Throws(DeviceNotFoundException::class)
    override fun setSignal(id: Long, reverse: Boolean, newSignal: Int): PwmSignal {
        val newSignalInverted = if (reverse) GpioManager.pwmRange - newSignal else newSignal
        val currSignal = controller.setSignal(getPin(id), newSignalInverted)
        return PwmSignal(id, if (reverse) GpioManager.pwmRange - currSignal else currSignal)
    }

    @Throws(DeviceNotFoundException::class)
    private fun getPin(id: Long): GpioPinPwmOutput {
        return map[id] ?: throw DeviceNotFoundException(id)
    }
}