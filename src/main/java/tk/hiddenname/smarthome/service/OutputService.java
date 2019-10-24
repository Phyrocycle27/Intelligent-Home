package tk.hiddenname.smarthome.service;

public interface OutputService {

    void delete(Integer id);

    void save(Integer id, Integer gpio, String name, Boolean reverse);

    void update(Integer id, String name, Boolean reverse);
}
