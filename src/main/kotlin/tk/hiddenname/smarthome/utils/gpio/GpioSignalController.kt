package tk.hiddenname.smarthome.utils.gpio

import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.GpioPinPwmOutput
import com.pi4j.io.gpio.PinState
import com.pi4j.wiringpi.Gpio
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GpioSignalController {

    private val log: Logger = LoggerFactory.getLogger(GpioSignalController::class.java)

    fun setState(output: GpioPinDigitalOutput, state: Boolean): Boolean {
        val pin = Gpio.wpiPinToGpio(output.pin.address)
        val currState = output.state
        val newState = PinState.getState(state)
        if (newState == currState) log.debug("Pin was already $state") else {
            log.debug(String.format("GPIO %d status before: %s\n", pin, currState))
            output.state = newState
            log.debug(String.format("GPIO %d status after: %s\n", pin, output.state))
        }
        return output.state.isHigh
    }

    fun setSignal(output: GpioPinPwmOutput, signal: Int): Int {
        val pin = Gpio.wpiPinToGpio(output.pin.address)
        val currSignal = output.pwm
        if (signal == currSignal) log.debug("Pin was already $signal") else {
            log.debug(String.format("GPIO %d signal before: %s\n", pin, currSignal))
            output.pwm = signal
            log.debug(String.format("GPIO %d signal after: %s\n", pin, output.pwm))
        }
        return output.pwm
    }
}