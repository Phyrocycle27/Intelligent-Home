package tk.hiddenname.smarthome.service.task.impl.listener.impl

import com.pi4j.io.gpio.event.GpioPinListenerDigital
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.not_found.TriggerNotFoundException
import tk.hiddenname.smarthome.exception.not_specified.TriggerObjectPropertyNotSpecifiedException
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

    @Autowired
    lateinit var service: DigitalSensorService

    @Autowired
    lateinit var dbService: SensorDatabaseService

    private lateinit var triggerObject: ChangeDigitalSignalObject
    private lateinit var gpioListener: GpioPinListenerDigital
    private var delayCounter: DelayCounterThread? = null

    override fun register(triggerObject: TriggerObject) {
        log.info("Registering triggerObject in ChangeDigitalSignalListener...")

        this.triggerObject = triggerObject as ChangeDigitalSignalObject

        val sensorId = this.triggerObject.sensorId
            ?: throw TriggerObjectPropertyNotSpecifiedException("sensorId")
        val targetState = this.triggerObject.targetState
            ?: throw TriggerObjectPropertyNotSpecifiedException("targetState")

        val sensor = dbService.getOne(sensorId)
        gpioListener = service.addListener(this, sensor.id, targetState, sensor.signalInversion)

        log.info("Trigger registered!")
    }

    override fun update(triggerObject: TriggerObject) {
        unregister()
        register(triggerObject)
    }

    override fun unregister() {
        val sensorId = this.triggerObject.sensorId

        if (sensorId != null) {
            service.removeListener(gpioListener, sensorId)
        }
    }

    override fun trigger(flag: Boolean) {
        if (delayCounter == null) {
            if (flag) {
                delayCounter = DelayCounterThread(triggerObject.delay)
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
            listener.updateFlag(triggerObject.id, flag)
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