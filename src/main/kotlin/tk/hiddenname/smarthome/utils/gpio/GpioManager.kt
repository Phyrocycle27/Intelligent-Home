package tk.hiddenname.smarthome.utils.gpio

import com.pi4j.io.gpio.*
import com.pi4j.wiringpi.Gpio
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.exist.GpioPinBusyException
import tk.hiddenname.smarthome.exception.not_found.GpioPinNotFoundException
import tk.hiddenname.smarthome.exception.not_specified.GpioPinNotSpecifiedException
import tk.hiddenname.smarthome.exception.not_specified.SignalTypeNotSpecifiedException
import tk.hiddenname.smarthome.exception.support.GpioModeNotSupportsException
import tk.hiddenname.smarthome.exception.support.GpioModeNotSupportsWithSignalTypeException
import tk.hiddenname.smarthome.exception.support.PinSignalSupportException
import tk.hiddenname.smarthome.exception.support.UnsupportedSignalTypeException
import tk.hiddenname.smarthome.model.hardware.AvailableGpioPins
import tk.hiddenname.smarthome.model.hardware.GPIO
import tk.hiddenname.smarthome.model.hardware.GpioMode
import tk.hiddenname.smarthome.model.signal.SignalType

@Component
class GpioManager {

    companion object Configuration {
        @Suppress("unused")
        private val log = LoggerFactory.getLogger(GpioManager::class.java)
        private val controller = GpioFactory.getInstance()
        private val pwmOutputs = mutableSetOf(13, 19, 18, 12)
        private val digitalOutputs = mutableSetOf(
            4, 2, 3, 17, 27, 22, 10, 9, 11, 14, 15, 18, 23, 24,
            25, 8, 7, 1, 12, 16, 20, 21, 0, 5, 6, 13, 19, 26
        )
        private val digitalInputs = mutableSetOf(
            4, 2, 3, 17, 27, 22, 10, 9, 11, 14, 15, 18, 23, 24,
            25, 8, 7, 1, 12, 16, 20, 21, 0, 5, 6, 13, 19, 26
        )
        private val usedGpio = ArrayList<Int>()
        const val pwmRange = 1024
    }

    fun deletePin(pin: GpioPin) {
        pin.removeAllListeners()
        controller.unprovisionPin(pin)
        usedGpio.remove(Integer.valueOf(Gpio.wpiPinToGpio(pin.pin.address)))
    }

    fun checkAvailability(gpioPin: Int?) {
        gpioPin ?: throw GpioPinNotSpecifiedException()
        if (isExists(gpioPin)) {
            throw GpioPinBusyException(gpioPin)
        }
    }

    fun validate(gpioPin: Int?, signalType: SignalType?, pinMode: GpioMode): Boolean {
        gpioPin ?: throw GpioPinNotSpecifiedException()
        signalType ?: throw SignalTypeNotSpecifiedException()

        getPinByGpioPinNumber(gpioPin) // if gpio does not exists, we catch exception
        checkGpioPinSupportsSignalType(gpioPin, signalType)

        if (pinMode == GpioMode.INPUT) {
            if (signalType == SignalType.DIGITAL && !digitalInputs.contains(gpioPin)) {
                throw GpioModeNotSupportsException(gpioPin, pinMode)
            } else if (signalType != SignalType.DIGITAL) {
                throw GpioModeNotSupportsWithSignalTypeException(signalType, pinMode)
            }
        }

        return true
    }

    private fun checkGpioPinSupportsSignalType(gpioPin: Int, signalType: SignalType) {
        val supportsSignalType = when (signalType) {
            SignalType.DIGITAL -> digitalOutputs.contains(gpioPin)
            SignalType.PWM -> pwmOutputs.contains(gpioPin)
            else -> throw UnsupportedSignalTypeException(signalType)
        }

        if (!supportsSignalType) {
            throw PinSignalSupportException(gpioPin)
        }
    }

    private fun isExists(gpio: Int): Boolean {
        return usedGpio.contains(gpio)
    }

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

    fun createDigitalOutput(gpio: GPIO, reverse: Boolean?): GpioPinDigitalOutput {
        validate(gpio.gpioPin, gpio.signalType, gpio.pinMode)
        checkAvailability(gpio.gpioPin)

        val pin =
            controller.provisionDigitalOutputPin(getPinByGpioPinNumber(gpio.gpioPin!!), PinState.getState(reverse!!))
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_OUTPUT)
        usedGpio.add(gpio.gpioPin)
        return pin
    }

    fun createDigitalInput(gpio: GPIO): GpioPinDigitalInput {
        validate(gpio.gpioPin, gpio.signalType, gpio.pinMode)
        checkAvailability(gpio.gpioPin)

        val pin =
            controller.provisionDigitalInputPin(getPinByGpioPinNumber(gpio.gpioPin!!), PinPullResistance.PULL_DOWN)
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.PULL_DOWN, PinMode.DIGITAL_INPUT)
        usedGpio.add(gpio.gpioPin)
        return pin
    }

    fun createPwmOutput(gpio: GPIO, reverse: Boolean): GpioPinPwmOutput {
        validate(gpio.gpioPin, gpio.signalType, gpio.pinMode)
        checkAvailability(gpio.gpioPin)

        val pin = controller.provisionPwmOutputPin(getPinByGpioPinNumber(gpio.gpioPin!!), if (reverse) pwmRange else 0)
        pin.setPwmRange(pwmRange)
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF)
        usedGpio.add(gpio.gpioPin)
        return pin
    }

    fun getAvailableGpioPins(type: SignalType): AvailableGpioPins {
        val available = HashSet<Int>()
        val busy = when (type) {
            SignalType.DIGITAL -> digitalOutputs
            SignalType.PWM -> pwmOutputs
            else -> throw UnsupportedSignalTypeException(type)
        }

        busy.forEach {
            if (!usedGpio.contains(it)) {
                available.add(it)
            }
        }

        return AvailableGpioPins(available)
    }

    fun shutdown() {
        controller.shutdown()
    }
}