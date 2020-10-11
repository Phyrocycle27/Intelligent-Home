package tk.hiddenname.smarthome.model.signal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
abstract class Signal {


    @Getter
    @NotNull
    private final Long id;
}
