package tk.hiddenname.smarthome.entity.signal;

import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

@EqualsAndHashCode(of = {"outputId"}, callSuper = false)
abstract class Signal extends ResourceSupport {

    private Integer outputId;

    Signal(Integer outputId) {
        this.outputId = outputId;
    }

    public Integer getOutputId() {
        return outputId;
    }
}
