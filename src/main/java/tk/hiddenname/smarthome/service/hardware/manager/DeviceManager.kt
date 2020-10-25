package tk.hiddenname.smarthome.service.hardware.manager

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.GpioBusyException
import tk.hiddenname.smarthome.exception.GpioNotSpecifiedException
import tk.hiddenname.smarthome.exception.PinSignalSupportException
import tk.hiddenname.smarthome.exception.SignalTypeNotSupportsException
import tk.hiddenname.smarthome.model.hardware.Device
import tk.hiddenname.smarthome.model.signal.SignalType
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService
import tk.hiddenname.smarthome.service.hardware.impl.digital.output.DigitalDeviceService
import tk.hiddenname.smarthome.service.hardware.impl.pwm.output.PwmDeviceServiceImpl

@Component
class DeviceManager(private val digitalService: DigitalDeviceService,
                    private val pwmService: PwmDeviceServiceImpl,
                    private val service: DeviceDatabaseService) {

    private val log = LoggerFactory.getLogger(DeviceManager::class.java)

    @Throws(PinSignalSupportException::class, GpioBusyException::class)
    fun register(device: Device) {
        log.debug("Creating device $device")
        val gpio = device.gpio ?: throw GpioNotSpecifiedException()
        getService(gpio.type).save(
                device.id,
                gpio.gpioPin,
                device.signalInversion
        )
    }

    fun update(device: Device) {
        log.debug("Updating device $device")
        val gpio = device.gpio ?: throw GpioNotSpecifiedException()
        getService(gpio.type).update(
                device.id,
                device.signalInversion
        )
    }

    fun unregister(device: Device) {
        log.debug("Deleting device $device")
        val gpio = device.gpio ?: throw GpioNotSpecifiedException()
        getService(gpio.type).delete(
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
        log.info("Devices loaded")
    }

    private fun getService(type: SignalType): GPIOService {
        return when (type) {
            SignalType.DIGITAL -> digitalService
            SignalType.PWM -> pwmService
            else -> throw SignalTypeNotSupportsException(type)
        }
    }
}