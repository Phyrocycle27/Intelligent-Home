package tk.hiddenname.smarthome.service;

import tk.hiddenname.smarthome.entity.output.PwmOutput;

import java.util.List;

public interface PwmOutputService {
    void delete ();
    void save();
    void update();
    PwmOutput getOne(Integer id);
    List<PwmOutput> getAll();
}
