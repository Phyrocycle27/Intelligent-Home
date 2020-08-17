package tk.hiddenname.smarthome.entity.signal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
abstract class Signal {

    @NotNull
    @Getter
    private final Integer id;
}
