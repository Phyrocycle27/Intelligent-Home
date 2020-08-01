package tk.hiddenname.smarthome.service.task.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.task.processing.objects.DeviceSetSignalObject;
import tk.hiddenname.smarthome.repository.DeviceRepository;
import tk.hiddenname.smarthome.service.hardware.digital.output.DigitalDeviceService;

@Component
@Scope(scopeName = "prototype")
public class SetDigitalSignalProcessor extends SetSignalProcessor {

    private final DigitalDeviceService service;

    @Autowired
    public SetDigitalSignalProcessor(DeviceRepository repository, DigitalDeviceService service) {
        super(repository);
        this.service = service;
    }

    @Override
    public void setSignal(DeviceSetSignalObject object) {

    }
}
