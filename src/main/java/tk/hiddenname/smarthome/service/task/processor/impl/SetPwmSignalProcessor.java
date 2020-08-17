package tk.hiddenname.smarthome.service.task.processor.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.entity.task.processing.objects.SetPwmSignalObject;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.UnsupportedObjectTypeException;
import tk.hiddenname.smarthome.repository.DeviceRepository;
import tk.hiddenname.smarthome.service.hardware.pwm.output.PwmDeviceService;
import tk.hiddenname.smarthome.service.task.processor.Processor;

@Component
@AllArgsConstructor
@Scope(scopeName = "prototype")
public class SetPwmSignalProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(SetPwmSignalProcessor.class);
    private final PwmDeviceService service;
    private final DeviceRepository repository;

    private SetPwmSignalObject object;

    @Override
    public void process() {
        new Thread(() -> {
            Device device = repository.findById(object.getDeviceId()).orElseThrow(() -> {
                DeviceNotFoundException e = new DeviceNotFoundException(object.getDeviceId());
                log.warn(e.getMessage());
                return e;
            });

            int currSignal = service.getSignal(device.getId(), device.isReverse()).getPwmSignal();
            if (currSignal != object.getTargetSignal()) {
                log.info(String.format(" * Pwm signal (%d) will be set to device with id (%d) on GPIO " +
                                "(%d) for (%d) seconds",
                        object.getTargetSignal(), device.getId(), device.getGpio().getGpio(), object.getDelay()));
                service.setSignal(device.getId(), device.isReverse(), object.getTargetSignal());

                if (object.getDelay() > 0) {
                    try {
                        Thread.sleep(object.getDelay() * 1000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                    service.setSignal(device.getId(), device.isReverse(), currSignal);
                    log.info(String.format("* Processing complete! Pwm signal (%d) will be set to device " +
                                    "with id (%d) on GPIO (%d)",
                            currSignal, device.getId(), device.getGpio().getGpio()));
                }
            } else {
                log.info(String.format(" * Pwm signal on device with id (%d) on gpio (%d) have been already " +
                                "(%d). Nothing to change",
                        device.getId(), device.getGpio().getGpio(), object.getTargetSignal()));
            }
        }).start();
    }

    @Override
    public void register(ProcessingObject object) throws UnsupportedObjectTypeException {
        if (object instanceof SetPwmSignalObject) {
            this.object = (SetPwmSignalObject) object;
        } else {
            throw new UnsupportedObjectTypeException();
        }
    }
}
