package tk.hiddenname.smarthome.utils.gpio

import com.pi4j.io.gpio.*
import com.pi4j.wiringpi.Gpio
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.*
import tk.hiddenname.smarthome.model.hardware.AvailableGpioPins
import tk.hiddenname.smarthome.model.hardware.GPIO
import tk.hiddenname.smarthome.model.signal.SignalType

@Component
class GpioManager {

    companion object Configuration {
        private val log = LoggerFactory.getLogger(GpioManager::class.java)
        private val controller = GpioFactory.getInstance()
        private val pwmGPIO: MutableSet<Int> = mutableSetOf(13, 19, 18, 12)
        private val digitalGPIO: MutableSet<Int> = mutableSetOf(4,
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

    @Throws(PinSignalSupportException::class, SignalTypeNotSupportsException::class,
            GpioPinNotSpecifiedException::class)
    fun checkGpioPinSignalTypeSupports(gpio: GPIO, type: SignalType): Boolean {
        gpio.gpioPin ?: throw GpioPinNotSpecifiedException()
        if (!isSupports(type, gpio.gpioPin)) {
            throw PinSignalSupportException(gpio.gpioPin)
        }
        return true
    }

    @Throws(GpioBusyException::class, GpioPinNotSpecifiedException::class)
    fun checkAvailability(gpio: GPIO) {
        gpio.gpioPin ?: throw GpioPinNotSpecifiedException()
        if (isExists(gpio.gpioPin)) {
            throw GpioBusyException(gpio.gpioPin)
        }
    }

    @Throws(GpioPinNotFoundException::class, SignalTypeNotSupportsException::class)
    private fun isSupports(type: SignalType, gpioPin: Int): Boolean {
        getPinByGpioPinNumber(gpioPin)
        return when (type) {
            SignalType.DIGITAL -> digitalGPIO.contains(gpioPin)
            SignalType.PWM -> pwmGPIO.contains(gpioPin)
            else -> throw SignalTypeNotSupportsException(type)
        }
    }

    private fun isExists(gpio: Int): Boolean {
        return usedGPIO.contains(gpio)
    }

    @Throws(GpioPinNotFoundException::class)
    private fun getPinByGpioPinNumber(gpioNumber: Int): Pin {
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
            else -> throw GpioPinNotFoundException(gpioNumber)
        }
    }

    @Throws(GpioBusyException::class, PinSignalSupportException::class, GpioPinNotSpecifiedException::class,
            SignalTypeNotSupportsException::class, GpioPinNotFoundException::class)
    fun createDigitalOutput(gpio: GPIO, reverse: Boolean?): GpioPinDigitalOutput {
        gpio.gpioPin ?: throw GpioPinNotSpecifiedException()
        checkAvailability(gpio)
        checkGpioPinSignalTypeSupports(gpio, SignalType.DIGITAL)

        val pin = controller.provisionDigitalOutputPin(getPinByGpioPinNumber(gpio.gpioPin), PinState.getState(reverse!!))
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_OUTPUT)
        usedGPIO.add(gpio.gpioPin)
        return pin
    }

    @Throws(GpioBusyException::class, PinSignalSupportException::class, GpioPinNotSpecifiedException::class,
            SignalTypeNotSupportsException::class, GpioPinNotFoundException::class)
    fun createDigitalInput(gpio: GPIO): GpioPinDigitalInput {
        gpio.gpioPin ?: throw GpioPinNotSpecifiedException()
        checkAvailability(gpio)
        checkGpioPinSignalTypeSupports(gpio, SignalType.DIGITAL)

        val pin = controller.provisionDigitalInputPin(getPinByGpioPinNumber(gpio.gpioPin), PinPullResistance.PULL_DOWN)
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_INPUT)
        usedGPIO.add(gpio.gpioPin)
        return pin
    }

    @Throws(GpioBusyException::class, PinSignalSupportException::class, GpioPinNotSpecifiedException::class,
            SignalTypeNotSupportsException::class, GpioPinNotFoundException::class)
    fun createPwmOutput(gpio: GPIO, reverse: Boolean): GpioPinPwmOutput {
        gpio.gpioPin ?: throw GpioPinNotSpecifiedException()
        checkAvailability(gpio)
        checkGpioPinSignalTypeSupports(gpio, SignalType.PWM)

        val pin = controller.provisionPwmOutputPin(getPinByGpioPinNumber(gpio.gpioPin), if (reverse) pwmRange else 0)
        pin.setPwmRange(pwmRange)
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF)
        usedGPIO.add(gpio.gpioPin)
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