package tk.hiddenname.smarthome.entity.signal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
abstract class Signal {

    @Getter
    private final Integer id;
}
