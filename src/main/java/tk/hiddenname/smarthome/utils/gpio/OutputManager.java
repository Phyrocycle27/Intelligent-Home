package tk.hiddenname.smarthome.utils.gpio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.Output;
import tk.hiddenname.smarthome.exception.OutputAlreadyExistException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.TypeNotFoundException;
import tk.hiddenname.smarthome.service.OutputService;
import tk.hiddenname.smarthome.service.digital.DigitalOutputServiceImpl;
import tk.hiddenname.smarthome.service.pwm.PwmOutputServiceImpl;

@Component
public class OutputManager {

    public static final Logger log;
    private static DigitalOutputServiceImpl digitalService;
    private static PwmOutputServiceImpl pwmService;

    static {
        log = LoggerFactory.getLogger(OutputManager.class.getName());
    }

    @Autowired
    public OutputManager(DigitalOutputServiceImpl digitalService, PwmOutputServiceImpl pwmService) {
        OutputManager.digitalService = digitalService;
        OutputManager.pwmService = pwmService;
        log.debug("OutputManager's fields have just initialized");
    }

    public void create(Output output) throws PinSignalSupportException, OutputAlreadyExistException {
        log.debug("Creating output... " + output.toString());
        getService(output.getType()).save(
                output.getOutputId(),
                output.getGpio(),
                output.getName(),
                output.getReverse()
        );
    }

    public void update(Output output) {
        getService(output.getType()).update(
                output.getOutputId(),
                output.getName(),
                output.getReverse()
        );
    }

    public void delete(Output output) {
        getService(output.getType()).delete(
                output.getOutputId()
        );
    }

    private OutputService getService(String type) {
        switch (type) {
            case "digital":
                return digitalService;
            case "pwm":
                return pwmService;
            default:
                TypeNotFoundException e = new TypeNotFoundException(type);
                log.warn(e.getMessage());
                throw e;
        }
    }
}
