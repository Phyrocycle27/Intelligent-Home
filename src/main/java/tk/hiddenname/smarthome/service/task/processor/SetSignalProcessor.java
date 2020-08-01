package tk.hiddenname.smarthome.service.task.processor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.task.processing.objects.DeviceSetSignalObject;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.repository.DeviceRepository;

@RequiredArgsConstructor
public abstract class SetSignalProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(SetSignalProcessor.class);

    @NonNull
    private final DeviceRepository repository;

    private DeviceSetSignalObject object;

    public void setObject(DeviceSetSignalObject object) {
        this.object = object;
    }

    @Override
    public void process() {
        new Thread(() -> {
            Device device = repository.findById(object.getDeviceId()).orElseThrow(() -> {
                DeviceNotFoundException e = new DeviceNotFoundException(object.getDeviceId());
                log.warn(e.getMessage());
                return e;
            });

            setSignal(object);

            /*switch (object.getSignalType()) {
                case DIGITAL:
                    boolean currState = digitalService.getState(object.getDeviceId(), device.getReverse()).getDigitalState();
                    if (currState != (object.getTargetSignal() == 1)) {
                        log.info(String.format(" * Digital state (%b) will be set to device with id (%d) on GPIO " +
                                        "(%d) for (%d) seconds",
                                targetSignal, deviceId, device.getGpio().getGpio(), delay));
                        digitalService.setState(device.getId(), device.getReverse(), targetSignal == 1);

                        if (object.getDelay() > 0) {
                            try {
                                Thread.sleep(delay * 1000);
                            } catch (InterruptedException e) {
                                log.error(e.getMessage());
                            }
                            digitalService.setState(device.getId(), device.getReverse(), currState);
                            log.info(String.format("* Processing complete! Digital state (%b) will be set to device " +
                                            "with id (%d) on GPIO (%d)",
                                    currState, deviceId, device.getGpio().getGpio()));
                        }
                    } else {
                        log.info(String.format(" * Digital state on device with id (%d) on gpio (%d) have been already " +
                                        "(%b). Nothing to change",
                                deviceId, device.getGpio().getGpio(), targetSignal));
                    }
                    break;
                case PWM:
                    int currSignal = pwmService.getSignal(deviceId, device.getReverse()).getPwmSignal();
                    if (currSignal != targetSignal) {
                        log.info(String.format(" * PWM signal (%d) will be set to device with id (%d) on GPIO (%d) for (%d) seconds",
                                targetSignal, deviceId, device.getGpio().getGpio(), delay));
                        pwmService.setSignal(device.getId(), device.getReverse(), targetSignal);

                        if (delay > 0) {
                            try {
                                Thread.sleep(delay * 1000);
                            } catch (InterruptedException e) {
                                log.error(e.getMessage());
                            }
                            pwmService.setSignal(device.getId(), device.getReverse(), currSignal);
                            log.info(String.format("* Processing complete! PWM signal (%d) will be set to device with id (%d) on GPIO (%d)",
                                    currSignal, deviceId, device.getGpio().getGpio()));
                        }
                    } else {
                        log.info(String.format(" * PWM signal on device with id (%d) on gpio (%d) have been already (%d). Nothing to change",
                                deviceId, device.getGpio().getGpio(), targetSignal));
                    }
                    break;
                default:
                    log.error("Unsupported signal type", new UnsupportedSignalTypeException(signalType.name()));
            }*/
        }).start();
    }

    public abstract void setSignal(DeviceSetSignalObject object);
}
