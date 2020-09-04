package tk.hiddenname.smarthome.service.task.impl.processor.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.entity.task.processing.objects.SetDigitalSignalObject;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.exception.UnsupportedProcessingObjectTypeException;
import tk.hiddenname.smarthome.repository.DeviceRepository;
import tk.hiddenname.smarthome.service.hardware.impl.digital.output.DigitalDeviceService;
import tk.hiddenname.smarthome.service.task.impl.processor.Processor;

@Component
@AllArgsConstructor
@Scope(scopeName = "prototype")
public class SetDigitalSignalProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(SetDigitalSignalProcessor.class);
    private final DigitalDeviceService service;
    private final DeviceRepository repository;

    private SetDigitalSignalObject object;

    @Override
    public void process() {
        new Thread(() -> {
            Device device = repository.findById(object.getDeviceId()).orElseThrow(() -> {
                DeviceNotFoundException e = new DeviceNotFoundException(object.getDeviceId());
                log.warn(e.getMessage());
                return e;
            });

            boolean currState = service.getState(device.getId(), device.isReverse()).isDigitalState();
            if (currState != object.isTargetState()) {
                log.info(String.format(" * Digital state (%b) will be set to device with id (%d) on GPIO " +
                                "(%d) for (%d) seconds",
                        object.isTargetState(), device.getId(), device.getGpio().getGpio(), object.getDelay()));
                service.setState(device.getId(), device.isReverse(), object.isTargetState());

                if (object.getDelay() > 0) {
                    try {
                        Thread.sleep(object.getDelay() * 1000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                    service.setState(device.getId(), device.isReverse(), currState);
                    log.info(String.format("* Processing complete! Digital state (%b) will be set to device " +
                                    "with id (%d) on GPIO (%d)",
                            currState, device.getId(), device.getGpio().getGpio()));
                }
            } else {
                log.info(String.format(" * Digital state on device with id (%d) on gpio (%d) have been already " +
                                "(%b). Nothing to change",
                        device.getId(), device.getGpio().getGpio(), object.isTargetState()));
            }
        }).start();
    }

    @Override
    public void register(ProcessingObject object) throws UnsupportedProcessingObjectTypeException {
        if (object instanceof SetDigitalSignalObject) {
            this.object = (SetDigitalSignalObject) object;
        } else {
            throw new UnsupportedProcessingObjectTypeException(object.getClass().getSimpleName());
        }
    }
}
