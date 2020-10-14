package tk.hiddenname.smarthome.controller.device

import lombok.AllArgsConstructor
import org.jetbrains.annotations.NotNull
import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.model.signal.DigitalState
import tk.hiddenname.smarthome.model.signal.PwmSignal
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService
import tk.hiddenname.smarthome.service.hardware.impl.digital.output.DigitalDeviceServiceImpl
import tk.hiddenname.smarthome.service.hardware.impl.pwm.output.PwmDeviceServiceImpl
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/devices/control"])
@AllArgsConstructor
class DeviceSignalRestController(private val dbService: DeviceDatabaseService,
                                 private val digitalService: DigitalDeviceServiceImpl,
                                 private val pwmService: PwmDeviceServiceImpl) {

    // ******************************** PWM *************************************************
    @GetMapping(value = ["/pwm"], produces = ["application/json"])
    fun getPwmSignal(@RequestParam(name = "id") id: Long?): PwmSignal {
        val device = dbService.getOne(id)
        return pwmService.getSignal(device.id, device.signalInversion)
    }

    @PutMapping(value = ["/pwm"], produces = ["application/json"])
    fun setPwmSignal(@RequestBody @NotNull signal: @Valid PwmSignal?): PwmSignal {
        val device = dbService.getOne(signal?.id)
        return pwmService.setSignal(device.id,
                device.signalInversion,
                signal!!.pwmSignal)
    }

    // ***************************** DIGITAL **************************************************
    @GetMapping(value = ["/digital"], produces = ["application/json"])
    fun getState(@RequestParam(name = "id") id: Long?): DigitalState {
        val device = dbService.getOne(id)
        return digitalService.getState(device.id, device.signalInversion)
    }

    @PutMapping(value = ["/digital"], produces = ["application/json"])
    fun setState(@RequestBody @NotNull state: @Valid DigitalState?): DigitalState {
        val device = dbService.getOne(state?.id)
        return digitalService.setState(device.id,
                device.signalInversion,
                state!!.isDigitalState
        )
    }
}