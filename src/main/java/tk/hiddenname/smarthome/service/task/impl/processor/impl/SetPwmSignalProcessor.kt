package tk.hiddenname.smarthome.service.task.impl.processor.impl

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.UnsupportedProcessingObjectTypeException
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject
import tk.hiddenname.smarthome.model.task.processing.objects.SetPwmSignalObject
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.pwm.output.PwmDeviceService
import tk.hiddenname.smarthome.service.task.impl.processor.Processor

@Component
@Scope(scopeName = "prototype")
class SetPwmSignalProcessor : Processor {

    private val log = LoggerFactory.getLogger(SetPwmSignalProcessor::class.java)

    private var processingObject: SetPwmSignalObject? = null

    private val dbService: DeviceDatabaseService? = null
    private val service: PwmDeviceService? = null

    override fun process() {
        Thread {
            val device = dbService?.getOne(processingObject!!.deviceId!!)
            val currSignal = service?.getSignal(device?.id!!, device.signalInversion)?.pwmSignal
            if (currSignal != processingObject!!.targetSignal) {
                log.info(String.format(" * Pwm signal (%d) will be set to device with id (%d) on GPIO " +
                        "(%d) for (%d) seconds",
                        processingObject!!.targetSignal, device!!.id, device.gpio!!.gpioPin,
                        processingObject!!.delay))
                service?.setSignal(device.id, device.signalInversion, processingObject!!.targetSignal!!)
                if (processingObject!!.delay > 0) {
                    try {
                        Thread.sleep(processingObject!!.delay * 1000.toLong())
                    } catch (e: InterruptedException) {
                        log.error(e.message)
                    }
                    if (currSignal != null) {
                        service?.setSignal(device.id, device.signalInversion, currSignal)
                    }
                    log.info(String.format("* Processing complete! Pwm signal (%d) will be set to device " +
                            "with id (%d) on GPIO (%d)",
                            currSignal, device.id, device.gpio.gpioPin))
                }
            } else {
                log.info(String.format(" * Pwm signal on device with id (%d) on gpio (%d) have been already " +
                        "(%d). Nothing to change",
                        device?.id, device?.gpio!!.gpioPin, processingObject!!.targetSignal))
            }
        }.start()
    }

    @Throws(UnsupportedProcessingObjectTypeException::class)
    override fun register(processingObject: ProcessingObject) {
        if (processingObject is SetPwmSignalObject) {
            this.processingObject = processingObject
        } else {
            throw UnsupportedProcessingObjectTypeException(processingObject.javaClass.simpleName)
        }
    }
}