package tk.hiddenname.smarthome.service.task.impl.processor;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.entity.task.processing.objects.SetSignalObject;
import tk.hiddenname.smarthome.exception.NoSuchProcessorException;
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;
import tk.hiddenname.smarthome.exception.UnsupportedObjectTypeException;
import tk.hiddenname.smarthome.service.task.impl.processor.impl.SetDigitalSignalProcessor;
import tk.hiddenname.smarthome.service.task.impl.processor.impl.SetPwmSignalProcessor;

@Component
@AllArgsConstructor
public class ProcessorFactory {

    private final ApplicationContext ctx;

    public Processor create(ProcessingObject object) throws NoSuchProcessorException, UnsupportedObjectTypeException {
        Processor processor;

        switch (object.getAction()) {
            case SET_SIGNAL:
                switch (((SetSignalObject) object).getSignalType()) {
                    case PWM:
                        processor = ctx.getBean(SetPwmSignalProcessor.class);
                        break;
                    case DIGITAL:
                        processor = ctx.getBean(SetDigitalSignalProcessor.class);
                        break;
                    default:
                        throw new SignalTypeNotFoundException(((SetSignalObject) object).getSignalType().name());
                }
                break;
            default:
                throw new NoSuchProcessorException();
        }
        processor.register(object);
        return processor;
    }
}
