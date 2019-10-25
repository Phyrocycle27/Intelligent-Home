package tk.hiddenname.smarthome.entity.signal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

@AllArgsConstructor
@EqualsAndHashCode(of = {"outputId"}, callSuper = false)
abstract class Signal extends ResourceSupport {

    @Getter
    private Integer outputId;
}
