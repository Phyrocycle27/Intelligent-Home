package tk.hiddenname.smarthome.service.task.impl.listener.impl

import com.pi4j.io.gpio.event.GpioPinListenerDigital
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.TriggerNotFoundException
import tk.hiddenname.smarthome.exception.UnsupportedTriggerObjectTypeException
import tk.hiddenname.smarthome.model.task.trigger.objects.ChangeDigitalSignalObject
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import tk.hiddenname.smarthome.service.database.SensorDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.digital.input.DigitalSensorService
import tk.hiddenname.smarthome.service.task.impl.listener.EventListener
import tk.hiddenname.smarthome.service.task.impl.listener.Listener

@Component
@Scope("prototype")
class ChangeDigitalSignalListener(private val listener: EventListener) : Listener {

    private val log = LoggerFactory.getLogger(ChangeDigitalSignalListener::class.java)

    private var triggerObject: ChangeDigitalSignalObject? = null
    private var gpioListener: GpioPinListenerDigital? = null
    private var delayCounter: DelayCounterThread? = null

    private var service: DigitalSensorService? = null
    private var dbService: SensorDatabaseService? = null

    @Autowired
    fun setService(service: DigitalSensorService) {
        this.service = service
    }

    @Autowired
    fun setDbService(dbService: SensorDatabaseService) {
        this.dbService = dbService
    }

    @Throws(UnsupportedTriggerObjectTypeException::class)
    override fun register(triggerObject: TriggerObject) {
        if (triggerObject is ChangeDigitalSignalObject) {
            this.triggerObject = triggerObject
            val sensor = dbService?.getOne(this.triggerObject!!.id)
            gpioListener = service?.addListener(this, sensor!!.id, this.triggerObject!!.targetState,
                    sensor.signalInversion)
        } else {
            throw UnsupportedTriggerObjectTypeException(triggerObject.javaClass.simpleName)
        }
    }

    @Throws(UnsupportedTriggerObjectTypeException::class)
    override fun update(triggerObject: TriggerObject) {
        unregister()
        register(triggerObject)
    }

    override fun unregister() {
        service!!.removeListener(gpioListener!!, triggerObject!!.sensorId)
    }

    override fun trigger(flag: Boolean) {
        if (delayCounter == null) {
            if (flag) {
                delayCounter = DelayCounterThread(triggerObject!!.delay)
                delayCounter!!.isDaemon = true
                delayCounter!!.start()
            }
        } else {
            if (!flag) {
                delayCounter!!.disable()
                setEventListenerFlag(false)
                delayCounter = null
            }
        }
    }

    private fun setEventListenerFlag(flag: Boolean) {
        try {
            listener.updateFlag(triggerObject!!.id, flag)
        } catch (e: TriggerNotFoundException) {
            unregister()
        }
    }

    private inner class DelayCounterThread(private var delay: Int) : Thread() {
        private var status = true
        fun disable() {
            status = false
        }

        override fun run() {
            while (status && delay > 0) {
                try {
                    sleep(1000)
                } catch (e: InterruptedException) {
                    log.error(e.message)
                }
                delay--
            }
            if (delay == 0) {
                setEventListenerFlag(true)
            }
        }
    }
}