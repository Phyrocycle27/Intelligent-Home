package tk.hiddenname.smarthome.service.task.impl.processor.impl

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.not_specified.ProcessingObjectPropertyNotSpecifiedException
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject
import tk.hiddenname.smarthome.model.task.processing.objects.SetDigitalSignalObject
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.digital.output.DigitalDeviceService
import tk.hiddenname.smarthome.service.task.impl.processor.Processor

@Component
@Scope(scopeName = "prototype")
class SetDigitalSignalProcessor : Processor {

    private val log = LoggerFactory.getLogger(SetDigitalSignalProcessor::class.java)

    @Autowired
    lateinit var service: DigitalDeviceService

    @Autowired
    lateinit var dbService: DeviceDatabaseService

    private lateinit var processingObject: SetDigitalSignalObject

    override fun process() {
        Thread {
            val device = dbService.getOne(processingObject.deviceId!!)
            val currState = service.getState(device.id, device.signalInversion).digitalState
            if (currState != processingObject.targetState) {
                service.setState(device.id, device.signalInversion, processingObject.targetState!!)
                log.info(
                    java.lang.String.format(
                        " * Digital state (%b) will be set to device with id (%d) on GPIO " +
                                "(%d) for (%d) seconds",
                        processingObject.targetState, device.id, device.gpio!!.gpioPin,
                        processingObject.delay
                    )
                )
                if (processingObject.delay > 0) {
                    try {
                        Thread.sleep(processingObject.delay * 1000L)
                    } catch (e: InterruptedException) {
                        log.error(e.message)
                    }
                    service.setState(device.id, device.signalInversion, currState!!)
                    log.info(
                        String.format(
                            "* Processing complete! Digital state (%b) will be set to device " +
                                    "with id (%d) on GPIO (%d)",
                            currState, device.id, device.gpio.gpioPin
                        )
                    )
                }
            } else {
                log.info(
                    java.lang.String.format(
                        " * Digital state on device with id (%d) on gpio (%d) have been already " +
                                "(%b). Nothing to change",
                        device.id, device.gpio!!.gpioPin, processingObject.targetState
                    )
                )
            }
        }.start()
    }

    override fun register(processingObject: ProcessingObject) {
        this.processingObject = processingObject as SetDigitalSignalObject
        val deviceId = processingObject.deviceId
            ?: throw ProcessingObjectPropertyNotSpecifiedException("deviceId")

        dbService.getOne(deviceId)
    }
}