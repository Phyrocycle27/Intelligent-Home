package tk.hiddenname.smarthome.entity.signal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
abstract class Signal {

    @Getter
    private Integer id;
}
