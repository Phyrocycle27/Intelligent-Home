package tk.hiddenname.smarthome.service;

import tk.hiddenname.smarthome.entity.Output;

public interface OutputService {

    void delete(Integer id);

    void save(Output newOutput);

    void update(Output newOutput);
}
