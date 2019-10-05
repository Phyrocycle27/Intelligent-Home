package tk.hiddenname.smarthome.service;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import tk.hiddenname.smarthome.entity.State;
import tk.hiddenname.smarthome.entity.output.DigitalOutput;

import java.util.List;

public interface DigitalOutputService {
    void delete(Integer id);

    void save(DigitalOutput newOutput);

    void update(DigitalOutput output, Integer id);

    GpioPinDigitalOutput getOne(Integer id);

    List<GpioPinDigitalOutput> getAll();

    State getState(Integer id);

    State setState(Integer id, Boolean newState);
}
