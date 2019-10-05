package tk.hiddenname.smarthome.entity;

import lombok.Data;

@Data
public class State {
    private Integer outputId;
    private Boolean state;

    public State(Integer outputId, Boolean state) {
        this.outputId = outputId;
        this.state = state;
    }
}
