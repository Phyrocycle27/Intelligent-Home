package tk.hiddenname.smarthome.entity.signal;

import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;

public enum SignalType {
    DIGITAL,
    PWM;

    public static SignalType getSignalType(String type) {
        try {
            return SignalType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SignalTypeNotFoundException(type);
        }
    }
}
