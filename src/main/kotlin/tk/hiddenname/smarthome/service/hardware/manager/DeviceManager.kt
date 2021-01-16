package tk.hiddenname.smarthome.service.hardware.manager

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.exist.GpioPinBusyException
import tk.hiddenname.smarthome.exception.not_specified.GpioNotSpecifiedException
import tk.hiddenname.smarthome.exception.not_specified.SignalTypeNotSpecifiedException
import tk.hiddenname.smarthome.exception.support.PinSignalSupportException
import tk.hiddenname.smarthome.exception.support.UnsupportedSignalTypeException
import tk.hiddenname.smarthome.model.hardware.Device
import tk.hiddenname.smarthome.model.signal.SignalType
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService
import tk.hiddenname.smarthome.service.hardware.impl.digital.output.DigitalDeviceService
import tk.hiddenname.smarthome.service.hardware.impl.pwm.output.PwmDeviceServiceImpl
import tk.hiddenname.smarthome.utils.gpio.GpioManager

@Component
class DeviceManager(
    private val digitalService: DigitalDeviceService,
    private val pwmService: PwmDeviceServiceImpl,
    private val service: DeviceDatabaseService,
    private val gpioManager: GpioManager
) {

    private val log = LoggerFactory.getLogger(DeviceManager::class.java)

    @Throws(PinSignalSupportException::class, GpioPinBusyException::class)
    fun register(device: Device) {
        log.info("Creating device $device")
        device.gpio ?: throw GpioNotSpecifiedException()
        getService(device.gpio.signalType).save(
            device.id,
            device.gpio,
            device.signalInversion
        )
    }

    fun update(device: Device) {
        log.info("Updating device $device")
        device.gpio ?: throw GpioNotSpecifiedException()
        getService(device.gpio.signalType).update(
            device.id,
            device.signalInversion
        )
    }

    fun unregister(device: Device) {
        log.info("Deleting device $device")
        device.gpio ?: throw GpioNotSpecifiedException()
        getService(device.gpio.signalType).delete(
            device.id
        )
    }

    fun loadDevices() {
        service.getAll().forEach {
            try {
                register(it)
            } catch (e: PinSignalSupportException) {
                log.warn(e.message)
            }
        }
        log.debug("Devices loaded")
    }

    @Suppress("DuplicatedCode")
    fun changeSignalType(device: Device, newSignalType: SignalType?) {
        newSignalType ?: throw SignalTypeNotSpecifiedException()
        device.gpio ?: throw GpioNotSpecifiedException()
        if (gpioManager.validate(device.gpio.gpioPin, newSignalType, device.gpio.pinMode)) {
            unregister(device)
            device.gpio.signalType = newSignalType
            register(device)
        }
    }

    private fun getService(type: SignalType?): GPIOService {
        return when (type) {
            SignalType.DIGITAL -> digitalService
            SignalType.PWM -> pwmService
            else -> throw UnsupportedSignalTypeException(type!!)
        }
    }
}