package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.Output;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;
import tk.hiddenname.smarthome.exception.OutputAlreadyExistException;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.TypeNotFoundException;
import tk.hiddenname.smarthome.repository.OutputsRepository;
import tk.hiddenname.smarthome.service.digital.DigitalOutputServiceImpl;
import tk.hiddenname.smarthome.service.pwm.PwmOutputServiceImpl;
import tk.hiddenname.smarthome.utils.gpio.GPIO;
import tk.hiddenname.smarthome.utils.gpio.OutputManager;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping(value = {"/outputs"})
@AllArgsConstructor
public class OutputRestController {

    private final OutputsRepository repository; // repository which connects postgresDB to our program
    // services
    private final DigitalOutputServiceImpl digitalService;
    private final PwmOutputServiceImpl pwmService;
    // Gpio creator
    private final OutputManager manager;

    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Output> getAll(
            @RequestParam(name = "type", defaultValue = "", required = false) String type)
            throws TypeNotFoundException {

        type = type.toLowerCase();
        List<Output> devices;

        if (type.isEmpty()) {
            devices = repository.findAll(Sort.by("outputId"));
        } else if (GPIO.isType(type)) {
            devices = repository.findByType(type, Sort.by("outputId"));
        } else throw new TypeNotFoundException(type);

        return devices;
    }

    @GetMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Output getOne(@PathVariable Integer id) {
        return repository.findById(id).orElseThrow(() -> new OutputNotFoundException(id));
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Output create(@RequestBody Output newOutput) throws OutputAlreadyExistException,
            PinSignalSupportException, TypeNotFoundException {

        if (GPIO.isType(newOutput.getType())) {
            GPIO.validate(newOutput.getGpio(), newOutput.getType());

            newOutput.setCreationDate(LocalDateTime.now());
            newOutput = repository.save(newOutput);
            manager.create(newOutput);
            return newOutput;
        } else throw new TypeNotFoundException(newOutput.getType());
    }

    @PutMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public Output replace(@RequestBody Output newOutput, @PathVariable Integer id) {

        return repository.findById(id)
                .map(output -> {
                    BeanUtils.copyProperties(newOutput, output, "outputId, creationDate, type, gpio");
                    manager.update(output);
                    return repository.save(output);
                })
                .orElseThrow(() -> new OutputNotFoundException(id));
    }

    @DeleteMapping(value = {"/one/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        manager.delete(repository.findById(id)
                .orElseThrow(() -> new OutputNotFoundException(id)));

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = {"/available"}, produces = {"application/json"})
    public String getAvailableOutputs(@RequestParam(name = "type") String type) {
        JSONObject obj = new JSONObject();
        switch (type) {
            case "digital":
                return obj.put("available_gpios", new JSONArray(GPIO.getAvailableDigitalGpios())).toString();
            case "pwm":
                return obj.put("available_gpios", new JSONArray(GPIO.getAvailablePwmGpios())).toString();
            default:
                throw new TypeNotFoundException(type);
        }
    }

    /* **************************************************************************************
     ********************************** CONTROL *********************************************
     ************************************************************************************** */

    // ******************************** PWM *************************************************
    @GetMapping(value = {"/control/pwm"}, produces = {"application/json"})
    public PwmSignal getPwmSignal(@RequestParam(name = "id") Integer id) {
        Output output = repository.findById(id).orElseThrow(() -> new OutputNotFoundException(id));

        return pwmService.getSignal(id, output.getReverse());
    }

    @PutMapping(value = {"/control/pwm"}, produces = {"application/json"})
    public PwmSignal setPwmSignal(@RequestBody PwmSignal signal) {

        Output output = repository.findById(signal.getOutputId())
                .orElseThrow(() -> new OutputNotFoundException(signal.getOutputId()));

        return pwmService.setSignal(
                output.getOutputId(),
                output.getReverse(),
                signal.getPwmSignal());
    }

    // ***************************** DIGITAL **************************************************

    @GetMapping(value = {"/control/digital"}, produces = {"application/json"})
    public DigitalState getState(@RequestParam(name = "id") Integer id) {

        Output output = repository.findById(id).orElseThrow(() -> new OutputNotFoundException(id));

        return digitalService.getState(id, output.getReverse());
    }

    @PutMapping(value = {"/control/digital"}, produces = {"application/hal+json"})
    public DigitalState setState(@RequestBody DigitalState state) {

        Output output = repository.findById(state.getOutputId())
                .orElseThrow(() -> new OutputNotFoundException(state.getOutputId()));

        return digitalService.setState(
                output.getOutputId(),
                output.getReverse(),
                state.getDigitalState()
        );
    }
}