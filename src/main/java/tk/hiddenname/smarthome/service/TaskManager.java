package tk.hiddenname.smarthome.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.entity.task.processing.objects.DeviceSetSignalObject;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.SensorChangeSignalObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.service.digital.input.DigitalSensorServiceImpl;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TaskManager {

    private final Logger log = LoggerFactory.getLogger(TaskManager.class);

    private final Map<Integer, EventProcessor> processors = new HashMap<>();
    private final Map<Integer, EventListener> listeners = new HashMap<>();

    @NonNull
    private final DigitalSensorServiceImpl digitalSensorService;

    public EventProcessor getProcessor(Integer id) {
        return processors.getOrDefault(id, null);
    }

    public EventListener getListener(Integer id) {
        return listeners.getOrDefault(id, null);
    }

    public void add(Task task) {
        log.info("Created task" + task.toString());
        // SET UP TRIGGERS
        EventListener listener = new EventListener(task.getId(), this);

        // Берём все группы тригеров и проходимся по ним циклом
        for (TriggerObject source : task.getTriggerObjects()) {
            switch (source.getAction()) {
                case SENSOR_CHANGE_SIGNAL:
                    // затем проходимся по самим триггерам в зависимости от их типа и регистрируем их
                    SensorChangeSignalObject object = (SensorChangeSignalObject) source;

                    log.info("Sensor change signal" + object.toString());

                    if (object.getSignalType() == SignalType.DIGITAL) {
                        boolean triggerSignal = Boolean.parseBoolean(object.getTriggerSignal());
                        digitalSensorService.addListener(object.getSensorId(), object.getId(), listener, triggerSignal);
                        listener.add(object.getId());
                        log.info("Listener created");
                    }

                    break;
            }
        }

        // Теперь то же самое делаем с обработчиками
        EventProcessor processor = new EventProcessor(task.getId());

        for (ProcessingObject source : task.getProcessingObjects()) {
            switch (source.getAction()) {
                case DEVICE_SET_SIGNAL:
                    // проходимся по обработчикам в зависимости от их типа и регистрируем их
                    DeviceSetSignalObject object = (DeviceSetSignalObject) source;

                    log.info("Device set signal: " + object.toString());

                    if (object.getSignalType() == SignalType.DIGITAL) {
                        boolean targetSignal = Boolean.parseBoolean(object.getTargetSignal());
                        processor.add(object.getId(), new SetDigitalSignalProcessor(object.getDeviceId(), targetSignal));
                        log.info("Processor created");
                    }

                    break;
            }
        }

        // Сохраняем processor'ы и listener'ы
        processors.put(task.getId(), processor);
        listeners.put(task.getId(), listener);
    }
}
