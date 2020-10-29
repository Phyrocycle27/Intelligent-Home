package tk.hiddenname.smarthome.service.hardware.manager

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.*
import tk.hiddenname.smarthome.model.hardware.Sensor
import tk.hiddenname.smarthome.model.signal.SignalType
import tk.hiddenname.smarthome.service.database.SensorDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService
import tk.hiddenname.smarthome.service.hardware.impl.digital.input.DigitalSensorServiceImpl
import tk.hiddenname.smarthome.utils.gpio.GpioManager

@Component
class SensorManager(private val service: SensorDatabaseService,
                    private val digitalService: DigitalSensorServiceImpl,
                    private val gpioManager: GpioManager) {

    private val log = LoggerFactory.getLogger(SensorManager::class.java)

    @Throws(PinSignalSupportException::class, GpioBusyException::class)
    fun register(sensor: Sensor) {
        log.debug("Creating sensor $sensor")
        sensor.gpio ?: throw GpioNotSpecifiedException()
        getService(sensor.gpio.signalType).save(
                sensor.id,
                sensor.gpio,
                sensor.signalInversion
        )
    }

    fun unregister(sensor: Sensor) {
        log.debug("Deleting sensor $sensor")
        sensor.gpio ?: throw GpioNotSpecifiedException()
        getService(sensor.gpio.signalType).delete(
                sensor.id
        )
    }

    fun loadSensors() {
        service.getAll().forEach {
            try {
                register(it)
            } catch (e: PinSignalSupportException) {
                log.warn(e.message)
            } catch (e: GpioBusyException) {
                log.warn(e.message)
            }
        }
    }

    @Suppress("DuplicatedCode")
    fun changeSignalType(sensor: Sensor, newSignalType: SignalType?) {
        newSignalType ?: throw SignalTypeNotSpecifiedException()
        sensor.gpio ?: throw GpioNotSpecifiedException()
        log.info("Validating")
        if (gpioManager.checkGpioPinSignalTypeSupports(sensor.gpio, newSignalType)) {
            log.info("Changing")
            unregister(sensor)
            sensor.gpio.signalType = newSignalType
            register(sensor)
        }
    }

    private fun getService(type: SignalType?): GPIOService {
        return when (type) {
            SignalType.DIGITAL -> digitalService
            else -> throw SignalTypeNotSupportsException(type!!)
        }
    }
}