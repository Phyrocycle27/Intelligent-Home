package tk.hiddenname.smarthome.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tk.hiddenname.smarthome.entity.listening.ListeningObject;
import tk.hiddenname.smarthome.entity.processing.ProcessingObject;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class Task {

    private Integer id;
    private String name;
    private Map<String, Set<ListeningObject>> listeningObjects;
    private Map<String, Set<ProcessingObject>> processingObjects;
}
