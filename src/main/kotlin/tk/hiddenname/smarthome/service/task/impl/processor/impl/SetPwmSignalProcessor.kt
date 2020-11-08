package tk.hiddenname.smarthome.service.task.impl.processor.impl

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.not_specified.ProcessingObjectPropertyNotSpecifiedException
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject
import tk.hiddenname.smarthome.model.task.processing.objects.SetPwmSignalObject
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.pwm.output.PwmDeviceService
import tk.hiddenname.smarthome.service.task.impl.processor.Processor

@Component
@Scope(scopeName = "prototype")
class SetPwmSignalProcessor : Processor {

    private val log = LoggerFactory.getLogger(SetPwmSignalProcessor::class.java)

    @Autowired
    lateinit var dbService: DeviceDatabaseService

    @Autowired
    lateinit var service: PwmDeviceService

    private lateinit var processingObject: SetPwmSignalObject

    override fun process() {
        Thread {
            val device = dbService.getOne(processingObject.deviceId!!)
            val currSignal = service.getSignal(device.id, device.signalInversion).pwmSignal
            if (currSignal != processingObject.targetSignal) {
                log.info(String.format(" * Pwm signal (%d) will be set to device with id (%d) on GPIO " +
                        "(%d) for (%d) seconds",
                        processingObject.targetSignal, device.id, device.gpio!!.gpioPin,
                        processingObject.delay))
                service.setSignal(device.id, device.signalInversion, processingObject.targetSignal!!)
                if (processingObject.delay > 0) {
                    try {
                        Thread.sleep(processingObject.delay * 1000L)
                    } catch (e: InterruptedException) {
                        log.error(e.message)
                    }
                    if (currSignal != null) {
                        service.setSignal(device.id, device.signalInversion, currSignal)
                    }
                    log.info(String.format("* Processing complete! Pwm signal (%d) will be set to device " +
                            "with id (%d) on GPIO (%d)",
                            currSignal, device.id, device.gpio.gpioPin))
                }
            } else {
                log.info(String.format(" * Pwm signal on device with id (%d) on gpio (%d) have been already " +
                        "(%d). Nothing to change",
                        device.id, device.gpio!!.gpioPin, processingObject.targetSignal))
            }
        }.start()
    }

    override fun register(processingObject: ProcessingObject) {
        this.processingObject = processingObject as SetPwmSignalObject
        val deviceId = processingObject.deviceId
                ?: throw ProcessingObjectPropertyNotSpecifiedException("deviceId")

        dbService.getOne(deviceId)
    }
}