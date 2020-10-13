package tk.hiddenname.smarthome.service.task.impl.processor.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.exception.UnsupportedProcessingObjectTypeException;
import tk.hiddenname.smarthome.model.hardware.Device;
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.model.task.processing.objects.SetDigitalSignalObject;
import tk.hiddenname.smarthome.service.database.DeviceDatabaseService;
import tk.hiddenname.smarthome.service.hardware.impl.digital.output.DigitalDeviceService;
import tk.hiddenname.smarthome.service.task.impl.processor.Processor;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Scope(scopeName = "prototype")
public class SetDigitalSignalProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(SetDigitalSignalProcessor.class);

    @NonNull
    private final DigitalDeviceService service;
    @NonNull
    private final DeviceDatabaseService dbService;

    private SetDigitalSignalObject object;

    @Override
    public void process() {
        new Thread(() -> {
            Device device = dbService.getOne(object.getDeviceId());

            boolean currState = service.getState(device.getId(), device.getReverse()).isDigitalState();
            if (currState != object.isTargetState()) {
                service.setState(device.getId(), device.getReverse(), object.isTargetState());
                log.info(String.format(" * Digital state (%b) will be set to device with id (%d) on GPIO " +
                                "(%d) for (%d) seconds",
                        object.isTargetState(), device.getId(), Objects.requireNonNull(device.getGpio()).getGpioPin(),
                        object.getDelay()));

                if (object.getDelay() > 0) {
                    try {
                        Thread.sleep(object.getDelay() * 1000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                    service.setState(device.getId(), device.getReverse(), currState);
                    log.info(String.format("* Processing complete! Digital state (%b) will be set to device " +
                                    "with id (%d) on GPIO (%d)",
                            currState, device.getId(), device.getGpio().getGpioPin()));
                }
            } else {
                log.info(String.format(" * Digital state on device with id (%d) on gpio (%d) have been already " +
                                "(%b). Nothing to change",
                        device.getId(), Objects.requireNonNull(device.getGpio()).getGpioPin(), object.isTargetState()));
            }
        }).start();
    }

    @Override
    public void register(@NotNull ProcessingObject object) throws UnsupportedProcessingObjectTypeException {
        if (object instanceof SetDigitalSignalObject) {
            this.object = (SetDigitalSignalObject) object;
        } else {
            throw new UnsupportedProcessingObjectTypeException(object.getClass().getSimpleName());
        }
    }
}
