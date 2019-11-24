package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.controller.assembler.DigitalStateResourceAssembler;
import tk.hiddenname.smarthome.controller.assembler.OutputResourceAssembler;
import tk.hiddenname.smarthome.controller.assembler.PwmSignalResourceAssembler;
import tk.hiddenname.smarthome.entity.Output;
import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;
import tk.hiddenname.smarthome.exception.OutputAlreadyExistException;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.TypeNotFoundException;
import tk.hiddenname.smarthome.repository.OutputsRepository;
import tk.hiddenname.smarthome.service.OutputService;
import tk.hiddenname.smarthome.service.digital.DigitalOutputServiceImpl;
import tk.hiddenname.smarthome.service.pwm.PwmOutputServiceImpl;
import tk.hiddenname.smarthome.utils.gpio.GPIO;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping(value = {"/outputs"})
@AllArgsConstructor
public class OutputRestController {

    public static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(OutputRestController.class.getName());
    }

    private final OutputsRepository repository; // repository which connects postgresDB to our program
    private final OutputResourceAssembler assembler;
    // services
    private final DigitalOutputServiceImpl digitalService;
    private final PwmOutputServiceImpl pwmService;
    // State and signal resource assemblers
    private final DigitalStateResourceAssembler digitalStateAssembler;
    private final PwmSignalResourceAssembler pwmSignalAssembler;

    @GetMapping(produces = {"application/json"})
    public Resources<Resource<Output>> getAll(
            @RequestParam(name = "type", defaultValue = "", required = false) String type)
            throws TypeNotFoundException {

        type = type.toLowerCase();
        List<Resource<Output>> outputs;

        if (type.equals("")) {
            outputs = repository.findAll(Sort.by("outputId")).stream()
                    .map(assembler::toResource)
                    .collect(Collectors.toList());
        } else if (GPIO.isType(type)) {
            outputs = repository.findByType(type, Sort.by("outputId")).stream()
                    .map(assembler::toResource)
                    .collect(Collectors.toList());
        } else throw new TypeNotFoundException(type);

        return new Resources<>(outputs,
                linkTo(methodOn(OutputRestController.class).getAll(type)).withSelfRel());
    }

    @GetMapping(value = {"/output/{id}"}, produces = {"application/json"})
    public Resource<Output> getOne(@PathVariable Integer id) {

        return assembler.toResource(
                repository.findById(id)
                        .orElseThrow(() -> new OutputNotFoundException(id)));
    }

    @PostMapping(produces = {"application/json"})
    public ResponseEntity<?> create(@RequestBody Output newOutput)
            throws URISyntaxException, OutputAlreadyExistException, PinSignalSupportException, TypeNotFoundException {

        if (GPIO.isType(newOutput.getType())) {
            GPIO.validate(newOutput.getGpio(), newOutput.getType());

            newOutput.setCreationDate(LocalDateTime.now());
            newOutput = repository.save(newOutput);
            Objects.requireNonNull(getDataService(newOutput.getType())).save(
                    newOutput.getOutputId(),
                    newOutput.getGpio(),
                    newOutput.getName(),
                    newOutput.getReverse());

            LOGGER.log(Level.INFO, "Create new output " + newOutput.toString());

            Resource<Output> resource = assembler.toResource(newOutput);

            return ResponseEntity
                    .created(new URI(resource.getId().expand().getHref()))
                    .body(resource);
        } else throw new TypeNotFoundException(newOutput.getType());
    }

    @PutMapping(value = {"/output/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> replace(@RequestBody Output newOutput,
                                     @PathVariable Integer id) throws URISyntaxException {

        Output updatedOutput = repository.findById(id)
                .map(output -> {
                    BeanUtils.copyProperties(newOutput, output, "outputId, creationDate, type, gpio");

                    Objects.requireNonNull(getDataService(output.getType())).update(
                            output.getOutputId(),
                            output.getName(),
                            output.getReverse());

                    return repository.save(output);
                })
                .orElseThrow(() -> new OutputNotFoundException(id));

        Resource<Output> resource = assembler.toResource(updatedOutput);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping(value = {"/output/{id}"}, produces = {"application/json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        Objects.requireNonNull(getDataService(repository.findById(id)
                .orElseThrow(() -> new OutputNotFoundException(id))
                .getType())).delete(id);

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

    /* ****************************************************
     ********************** CONTROL ************************
     **************************************************** */

    // ****************** PWM ************************
    @GetMapping(value = {"/control/pwm"}, produces = {"application/json"})
    public Resource<PwmSignal> getPwmSignal(@RequestParam(name = "id") Integer id) {

        Output output = repository.findById(id).orElseThrow(() -> new OutputNotFoundException(id));

        return pwmSignalAssembler.toResource(pwmService.getSignal(output.getOutputId(), output.getReverse()));
    }

    @PutMapping(value = {"/control/pwm"}, produces = {"application/json"})
    public ResponseEntity<?> setPwmSignal(@RequestBody PwmSignal signal) throws URISyntaxException {

        Output output = repository.findById(signal.getOutputId())
                .orElseThrow(() -> new OutputNotFoundException(signal.getOutputId()));

        Resource<PwmSignal> resource = pwmSignalAssembler.toResource(pwmService.setSignal(
                output.getOutputId(),
                output.getReverse(),
                signal.getPwmSignal()
        ));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    // ********************************************************

    // ******************** DIGITAL *******************************

    @GetMapping(value = {"/control/digital"}, produces = {"application/json"})
    public Resource<DigitalState> getState(@RequestParam(name = "id") Integer id) {

        Output output = repository.findById(id).orElseThrow(() -> new OutputNotFoundException(id));

        return digitalStateAssembler.toResource(digitalService.getState(output.getOutputId(), output.getReverse()));
    }

    @PutMapping(value = {"/control/digital"}, produces = {"application/hal+json"})
    public ResponseEntity<?> setState(@RequestBody DigitalState state) throws URISyntaxException {

        Output output = repository.findById(state.getOutputId())
                .orElseThrow(() -> new OutputNotFoundException(state.getOutputId()));

        LOGGER.log(Level.INFO, "Reverse is: " + output.getReverse());
        Resource<DigitalState> resource = digitalStateAssembler.toResource(digitalService.setState(
                output.getOutputId(),
                output.getReverse(),
                state.getDigitalState()
        ));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    // ********************************************************

    private OutputService getDataService(String type) {
        switch (type) {
            case "digital":
                return digitalService;
            case "pwm":
                return pwmService;
            default:
                return null;
        }
    }
}