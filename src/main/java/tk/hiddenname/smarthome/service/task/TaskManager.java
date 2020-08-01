package tk.hiddenname.smarthome.service.task;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.entity.task.processing.objects.DeviceSetSignalObject;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.SensorChangeSignalObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.repository.DeviceRepository;
import tk.hiddenname.smarthome.service.hardware.digital.input.DigitalSensorServiceImpl;
import tk.hiddenname.smarthome.service.hardware.digital.output.DigitalDeviceServiceImpl;
import tk.hiddenname.smarthome.service.task.listener.EventListener;
import tk.hiddenname.smarthome.service.task.processor.EventProcessor;
import tk.hiddenname.smarthome.service.task.processor.SetSignalProcessor;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class TaskManager {

    private final Logger log = LoggerFactory.getLogger(TaskManager.class);

    private final Map<Integer, EventProcessor> processors = new HashMap<>();
    private final Map<Integer, EventListener> listeners = new HashMap<>();

    private final DigitalSensorServiceImpl digitalSensorService;
    private final DigitalDeviceServiceImpl digitalDeviceService;
    private final DeviceRepository deviceRepo;

    public EventProcessor getProcessor(Integer id) {
        return processors.getOrDefault(id, null);
    }

    public EventListener getListener(Integer id) {
        return listeners.getOrDefault(id, null);
    }

    public void add(Task task) {
        log.info("******* Creating Task *********");
        // SET UP TRIGGERS

        // Теперь то же самое делаем с обработчиками
        EventProcessor processor = new EventProcessor(task.getId());
        for (ProcessingObject source : task.getProcessingObjects()) {
            switch (source.getAction()) {
                case DEVICE_SET_SIGNAL:
                    // проходимся по обработчикам в зависимости от их типа и регистрируем их
                    DeviceSetSignalObject object = (DeviceSetSignalObject) source;

                    log.info("DO: Device set signal (" + object.toString() + ")");

                    processor.add(object.getId(), new SetSignalProcessor(digitalDeviceService, deviceRepo,
                            object.getDeviceId()));
                    log.info("* Processor created");
            }

            break;
        }


        EventListener listener = new EventListener(task.getId(), processor);
        // Берём все группы тригеров и проходимся по ним циклом
        for (TriggerObject source : task.getTriggerObjects()) {
            switch (source.getAction()) {
                case SENSOR_CHANGE_SIGNAL:
                    // затем проходимся по самим триггерам в зависимости от их типа и регистрируем их
                    SensorChangeSignalObject object = (SensorChangeSignalObject) source;

                    log.info("TRIGGER:" + object.toString());

                    if (object.getSignalType() == SignalType.DIGITAL) {
                        boolean triggerSignal = Boolean.parseBoolean(object.getTriggerSignal());
                        digitalSensorService.addListener(object.getSensorId(), object.getId(), listener, triggerSignal);
                        listener.add(object.getId());
                    }

                    break;
            }
        }

        // Сохраняем processor'ы и listener'ы
        processors.put(task.getId(), processor);
        listeners.put(task.getId(), listener);
    }

    public void loadTasks() {

    }
}
