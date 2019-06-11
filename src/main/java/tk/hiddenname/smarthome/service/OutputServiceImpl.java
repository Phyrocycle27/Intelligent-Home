package tk.hiddenname.smarthome.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entities.Output;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.repository.OutputRepository;

import java.util.List;

@Service
public class OutputServiceImpl implements OutputService {

    private OutputRepository repository;


    @Override
    public List<Output> getAllOutputs() {
        return repository.findAll();
    }

    @Override
    public Output getOutputById(Integer id) throws OutputNotFoundException {
        return repository.findById(id).
                orElseThrow(() -> new OutputNotFoundException(id));
    }

    @Override
    public Output saveOutput(Output output) {
        return repository.save(output);
    }

    @Override
    public Output updateOutput(Integer id, Output outputDetails) throws OutputNotFoundException {
        Output outputFromDB = repository.findById(id).
                orElseThrow(() -> new OutputNotFoundException(id));

        BeanUtils.copyProperties(outputDetails, outputFromDB, "id");
        return repository.save(outputFromDB);
    }

    @Override
    public void deleteOutput(Integer id) throws OutputNotFoundException {
        Output output = repository.findById(id).
                orElseThrow(() -> new OutputNotFoundException(id));
        repository.delete(output);
    }

    @Autowired
    public void setRepository(OutputRepository repository) {
        this.repository = repository;
    }
}