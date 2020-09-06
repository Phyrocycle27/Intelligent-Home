package tk.hiddenname.smarthome.service.task.impl.processor;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.exception.NoSuchProcessorException;
import tk.hiddenname.smarthome.exception.UnsupportedProcessingObjectTypeException;
import tk.hiddenname.smarthome.exception.UnsupportedTriggerObjectTypeException;
import tk.hiddenname.smarthome.service.task.impl.processor.impl.SetDigitalSignalProcessor;
import tk.hiddenname.smarthome.service.task.impl.processor.impl.SetPwmSignalProcessor;

@Component
@AllArgsConstructor
public class ProcessorFactory {

    private final ApplicationContext ctx;

    public Processor create(ProcessingObject object) throws NoSuchProcessorException,
            UnsupportedTriggerObjectTypeException, UnsupportedProcessingObjectTypeException {

        Processor processor;

        switch (object.getAction()) {
            case SET_DIGITAL_SIGNAL:
                processor = ctx.getBean(SetDigitalSignalProcessor.class);
                break;
            case SET_PWM_SIGNAL:
                processor = ctx.getBean(SetPwmSignalProcessor.class);
                break;
            default:
                throw new NoSuchProcessorException();
        }
        processor.register(object);
        return processor;
    }
}
