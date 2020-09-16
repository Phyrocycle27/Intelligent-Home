package tk.hiddenname.smarthome.entity.hardware;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@ToString
@AllArgsConstructor
public class AvailableGpioPins {

    private final Set<Integer> availableGpioPins;
}
