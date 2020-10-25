package tk.hiddenname.smarthome.utils.gpio

import com.pi4j.io.gpio.*
import com.pi4j.wiringpi.Gpio
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.*
import tk.hiddenname.smarthome.model.hardware.AvailableGpioPins
import tk.hiddenname.smarthome.model.signal.SignalType

@Component
class GpioManager {

    companion object Configuration {
        private val controller = GpioFactory.getInstance()
        private val pwmGPIO: MutableSet<Int> = mutableSetOf(13, 19, 18, 12)
        private val digitalGPIO: MutableSet<Int> = mutableSetOf(
                2, 3, 17, 27, 22, 10, 9, 11, 14, 15, 18, 23, 24,
                25, 8, 7, 1, 12, 16, 20, 21, 0, 5, 6, 13, 19, 26
        )
        private val usedGPIO: MutableList<Int> = ArrayList()
        const val pwmRange = 1024
    }

    fun deletePin(pin: GpioPin) {
        controller.unprovisionPin(pin)
        usedGPIO.remove(Integer.valueOf(Gpio.wpiPinToGpio(pin.pin.address)))
        pin.removeAllListeners()
    }

    @Throws(PinSignalSupportException::class, SignalTypeNotFoundException::class, GpioBusyException::class)
    fun validate(gpio: Int, type: SignalType) {
        if (!isSupports(type, gpio)) {
            throw PinSignalSupportException(gpio)
        }
        if (isExists(gpio)) throw GpioBusyException(gpio)
    }

    private fun isSupports(type: SignalType, gpio: Int): Boolean {
        return when (type) {
            SignalType.DIGITAL -> digitalGPIO.contains(gpio)
            SignalType.PWM -> pwmGPIO.contains(gpio)
            else -> throw SignalTypeNotSupportsException(type)
        }
    }

    private fun isExists(gpio: Int): Boolean {
        return usedGPIO.contains(gpio)
    }

    @Throws(GpioNotFoundException::class)
    private fun getPinByGPIONumber(gpioNumber: Int): Pin? {
        return when (gpioNumber) {
            2 -> RaspiPin.GPIO_08
            3 -> RaspiPin.GPIO_09
            4 -> RaspiPin.GPIO_07
            17 -> RaspiPin.GPIO_00
            27 -> RaspiPin.GPIO_02
            22 -> RaspiPin.GPIO_03
            10 -> RaspiPin.GPIO_12
            9 -> RaspiPin.GPIO_13
            11 -> RaspiPin.GPIO_14
            0 -> RaspiPin.GPIO_30
            5 -> RaspiPin.GPIO_21
            6 -> RaspiPin.GPIO_22
            13 -> RaspiPin.GPIO_23
            19 -> RaspiPin.GPIO_24
            26 -> RaspiPin.GPIO_25
            14 -> RaspiPin.GPIO_15
            15 -> RaspiPin.GPIO_16
            18 -> RaspiPin.GPIO_01
            23 -> RaspiPin.GPIO_04
            24 -> RaspiPin.GPIO_05
            25 -> RaspiPin.GPIO_06
            8 -> RaspiPin.GPIO_10
            7 -> RaspiPin.GPIO_11
            1 -> RaspiPin.GPIO_31
            12 -> RaspiPin.GPIO_26
            16 -> RaspiPin.GPIO_27
            20 -> RaspiPin.GPIO_28
            21 -> RaspiPin.GPIO_29
            else -> throw GpioNotFoundException(gpioNumber)
        }
    }

    @Throws(GpioBusyException::class, PinSignalSupportException::class)
    fun createDigitalOutput(gpio: Int, reverse: Boolean?): GpioPinDigitalOutput {
        validate(gpio, SignalType.DIGITAL)
        val pin = controller.provisionDigitalOutputPin(
                getPinByGPIONumber(gpio), PinState.getState(reverse!!))
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_OUTPUT)
        usedGPIO.add(gpio)
        return pin
    }

    @Throws(GpioBusyException::class, PinSignalSupportException::class)
    fun createDigitalInput(gpio: Int): GpioPinDigitalInput {
        validate(gpio, SignalType.DIGITAL)
        val pin = controller.provisionDigitalInputPin(getPinByGPIONumber(gpio), PinPullResistance.PULL_DOWN)
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_INPUT)
        usedGPIO.add(gpio)
        return pin
    }

    @Throws(GpioBusyException::class, PinSignalSupportException::class)
    fun createPwmOutput(gpio: Int, reverse: Boolean): GpioPinPwmOutput {
        validate(gpio, SignalType.PWM)

        val pin = controller.provisionPwmOutputPin(getPinByGPIONumber(gpio), if (reverse) pwmRange else 0)
        pin.setPwmRange(pwmRange)
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF)
        usedGPIO.add(gpio)
        return pin
    }

    fun getAvailableGpioPins(type: SignalType): AvailableGpioPins {
        val available = HashSet<Int>()
        val busy = when (type) {
            SignalType.DIGITAL -> digitalGPIO
            SignalType.PWM -> pwmGPIO
            else -> throw UnsupportedSignalTypeException(type)
        }

        busy.forEach {
            if (!usedGPIO.contains(it)) {
                available.add(it)
            }
        }

        return AvailableGpioPins(available)
    }

    fun shutdown() {
        controller.shutdown()
    }
}