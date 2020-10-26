package tk.hiddenname.smarthome.service.hardware.manager

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.GpioBusyException
import tk.hiddenname.smarthome.exception.GpioNotSpecifiedException
import tk.hiddenname.smarthome.exception.PinSignalSupportException
import tk.hiddenname.smarthome.exception.SignalTypeNotSupportsException
import tk.hiddenname.smarthome.model.hardware.Sensor
import tk.hiddenname.smarthome.model.signal.SignalType
import tk.hiddenname.smarthome.service.database.SensorDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService
import tk.hiddenname.smarthome.service.hardware.impl.digital.input.DigitalSensorServiceImpl

@Component
class SensorManager(private val service: SensorDatabaseService, private val digitalService: DigitalSensorServiceImpl) {

    private val log = LoggerFactory.getLogger(SensorManager::class.java)

    @Throws(PinSignalSupportException::class, GpioBusyException::class)
    fun create(sensor: Sensor) {
        log.debug("Creating device $sensor")
        val gpio = sensor.gpio ?: throw GpioNotSpecifiedException()
        getService(gpio.signalType).save(
                sensor.id,
                gpio.gpioPin,
                sensor.signalInversion
        )
    }

    fun delete(sensor: Sensor) {
        log.debug("Deleting sensor $sensor")
        val gpio = sensor.gpio ?: throw GpioNotSpecifiedException()
        getService(gpio.signalType).delete(
                sensor.id
        )
    }

    fun loadSensors() {
        service.getAll().forEach {
            try {
                create(it)
            } catch (e: PinSignalSupportException) {
                log.warn(e.message)
            } catch (e: GpioBusyException) {
                log.warn(e.message)
            }
        }
    }

    private fun getService(type: SignalType): GPIOService {
        return when (type) {
            SignalType.DIGITAL -> digitalService
            else -> throw SignalTypeNotSupportsException(type)
        }
    }
}