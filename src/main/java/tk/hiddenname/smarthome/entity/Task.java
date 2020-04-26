package tk.hiddenname.smarthome.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Task {

    private Integer id;
    private String name;
    private ListeningAction listeningAction;
    private Object listeningObject;
    private TargetAction targetAction;
    private Object targetObject;
}
