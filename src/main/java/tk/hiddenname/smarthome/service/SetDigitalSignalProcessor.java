package tk.hiddenname.smarthome.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.repository.DeviceRepository;
import tk.hiddenname.smarthome.service.digital.output.DigitalDeviceServiceImpl;

@AllArgsConstructor
public class SetDigitalSignalProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(SetDigitalSignalProcessor.class);

    private static DigitalDeviceServiceImpl service;
    private static DeviceRepository repository;

    private final Integer deviceId;
    private final boolean targetSignal;

    @Autowired
    public static void setService(DigitalDeviceServiceImpl service) {
        SetDigitalSignalProcessor.service = service;
    }

    @Autowired
    public static void setRepository(DeviceRepository repository) {
        SetDigitalSignalProcessor.repository = repository;
    }

    @Override
    public void process() {
        Device device = repository.findById(deviceId).orElseThrow(() -> {
            DeviceNotFoundException e = new DeviceNotFoundException(deviceId);
            log.warn(e.getMessage());
            return e;
        });

        service.setState(device.getId(), device.getReverse(), targetSignal);
    }
}
