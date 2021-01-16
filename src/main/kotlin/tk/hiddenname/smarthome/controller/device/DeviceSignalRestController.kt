package tk.hiddenname.smarthome.controller.device

import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.exception.not_specified.HardwareIdNotSpecifiedException
import tk.hiddenname.smarthome.exception.not_specified.SignalValueNotSpecifiedException
import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.model.signal.PwmSignal
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.digital.output.DigitalDeviceServiceImpl
import tk.hiddenname.smarthome.service.hardware.impl.pwm.output.PwmDeviceServiceImpl
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/devices/control"])
class DeviceSignalRestController(
    private val dbService: DeviceDatabaseService,
    private val digitalService: DigitalDeviceServiceImpl,
    private val pwmService: PwmDeviceServiceImpl
) {

    // ******************************** PWM *************************************************
    @GetMapping(value = ["/pwm/{id}"], produces = ["application/json"])
    fun getPwmSignal(@PathVariable(name = "id", required = true) id: Long): PwmSignal {
        val device = dbService.getOne(id)
        return pwmService.getSignal(device.id, device.signalInversion)
    }

    @PutMapping(value = ["/pwm"], produces = ["application/json"])
    fun setPwmSignal(@Valid @RequestBody(required = true) signal: PwmSignal): PwmSignal {
        val device = dbService.getOne(signal.hardwareId ?: throw HardwareIdNotSpecifiedException())
        signal.pwmSignal ?: throw SignalValueNotSpecifiedException()
        return pwmService.setSignal(
            device.id,
            device.signalInversion,
            signal.pwmSignal
        )
    }

    // ***************************** DIGITAL **************************************************
    @GetMapping(value = ["/digital/{id}"], produces = ["application/json"])
    fun getState(@PathVariable(name = "id", required = true) id: Long): DigitalState {
        val device = dbService.getOne(id)
        return digitalService.getState(device.id, device.signalInversion)
    }

    @PutMapping(value = ["/digital"], produces = ["application/json"])
    fun setState(@Valid @RequestBody(required = true) state: DigitalState): DigitalState {
        val device = dbService.getOne(state.hardwareId ?: throw HardwareIdNotSpecifiedException())
        state.digitalState ?: throw SignalValueNotSpecifiedException()
        return digitalService.setState(
            device.id,
            device.signalInversion,
            state.digitalState
        )
    }
}