package tk.hiddenname.smarthome.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.entity.task.processing.ProcessingGroup;
import tk.hiddenname.smarthome.entity.task.processing.objects.DeviceSetSignalObject;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.entity.task.trigger.TriggerGroup;
import tk.hiddenname.smarthome.entity.task.trigger.objects.SensorChangeSignalObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.service.digital.input.DigitalSensorServiceImpl;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TaskManager {

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
        // SET UP TRIGGERS
        EventListener listener = new EventListener(task.getId(), this);
        // Берём все группы тригеров и проходимся по ним циклом
        for (TriggerGroup group : task.getTriggerGroups().values()) {
            switch (group.getAction()) {
                case SENSOR_CHANGE_SIGNAL:
                    // затем проходимся по самим триггерам в зависимости от их типа и регистрируем их
                    for (TriggerObject o1 : group.getTriggerObjects()) {
                        SensorChangeSignalObject o2 = (SensorChangeSignalObject) o1;

                        if (o2.getType() == SignalType.DIGITAL) {
                            boolean triggerSignal = Boolean.parseBoolean(o2.getTriggerSignal());
                            digitalSensorService.addListener(o2.getSensorId(), o2.getId(), listener, triggerSignal);
                        }

                        listener.add(o2.getId());
                    }
                    break;
            }
        }

        // Теперь то же самое делаем с обработчиками
        EventProcessor processor = new EventProcessor(task.getId());

        for (ProcessingGroup group : task.getProcessingGroups().values()) {
            switch (group.getAction()) {
                case DEVICE_SET_SIGNAL:
                    // проходимся по обработчикам в зависимости от их типа и регистрируем их
                    for (ProcessingObject o1 : group.getProcessingObjects()) {
                        DeviceSetSignalObject o2 = (DeviceSetSignalObject) o1;

                        if (o2.getType() == SignalType.DIGITAL) {
                            boolean targetSignal = Boolean.parseBoolean(o2.getTargetSignal());
                            processor.add(o2.getId(), new SetDigitalSignalProcessor(o2.getDeviceId(), targetSignal));
                        }
                    }
                    break;
            }
        }

        // Сохраняем processor'ы и listener'ы
        processors.put(task.getId(), processor);
        listeners.put(task.getId(), listener);
    }
}
