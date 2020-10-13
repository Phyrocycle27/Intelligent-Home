package tk.hiddenname.smarthome.model.signal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
abstract class Signal {

    @Getter
    public final Long id;
}
