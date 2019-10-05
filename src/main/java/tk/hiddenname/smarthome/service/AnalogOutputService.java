package tk.hiddenname.smarthome.service;

import tk.hiddenname.smarthome.entity.output.AnalogOutput;

import java.util.List;

public interface AnalogOutputService {
    void delete ();
    void save();
    void update();
    AnalogOutput getOne(Integer id);
    List<AnalogOutput> getAll();
}
