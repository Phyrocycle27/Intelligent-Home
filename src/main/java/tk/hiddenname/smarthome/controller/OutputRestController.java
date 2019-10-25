package tk.hiddenname.smarthome.controller;

import lombok.AllArgsConstructor;
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
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.exception.TypeNotFoundException;
import tk.hiddenname.smarthome.repository.OutputsRepository;
import tk.hiddenname.smarthome.service.OutputService;
import tk.hiddenname.smarthome.service.digital.DigitalOutputServiceImpl;
import tk.hiddenname.smarthome.service.pwm.PwmOutputServiceImpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping(value = {"/outputs"})
@AllArgsConstructor
public class OutputRestController {

    private final OutputsRepository repository; // repository which connects postgresDB to our program
    private final OutputResourceAssembler assembler;

    // services
    private final DigitalOutputServiceImpl digitalService;
    private final PwmOutputServiceImpl pwmService;

    // State and signal resource assemblers
    private final DigitalStateResourceAssembler digitalStateAssembler;
    private final PwmSignalResourceAssembler pwmSignalAssembler;

    @GetMapping(produces = {"application/hal+json"})
    public Resources<Resource<Output>> getAll(
            @RequestParam(name = "type", defaultValue = "", required = false) String type) {

        type = type.toLowerCase();
        List<Resource<Output>> outputs;

        if (type.equals("")) {
            outputs = repository.findAll(Sort.by("outputId")).stream()
                    .map(assembler::toResource)
                    .collect(Collectors.toList());
        } else if (type.equals("pwm") | type.equals("digital")) {
            outputs = repository.findByType(type, Sort.by("outputId")).stream()
                    .map(assembler::toResource)
                    .collect(Collectors.toList());
        } else throw new TypeNotFoundException(type);

        return new Resources<>(outputs,
                linkTo(methodOn(OutputRestController.class).getAll(type)).withSelfRel());
    }

    @GetMapping(value = {"/{id}"}, produces = {"application/hal+json"})
    public Resource<Output> getOne(@PathVariable Integer id) {

        return assembler.toResource(
                repository.findById(id)
                        .orElseThrow(() -> new OutputNotFoundException(id)));
    }

    @PostMapping(produces = {"application/hal+json"})
    public ResponseEntity<?> create(@RequestBody Output newOutput) throws URISyntaxException {

        newOutput.setCreationDate(LocalDateTime.now());

        getDataService(newOutput.getType()).save(
                newOutput.getOutputId(),
                newOutput.getGpio(),
                newOutput.getName(),
                newOutput.getReverse());

        Resource<Output> resource = assembler.toResource(repository.save(newOutput));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping(value = {"/{id}"}, produces = {"application/hal+json"})
    public ResponseEntity<?> replace(@RequestBody Output newOutput,
                                     @PathVariable Integer id) throws URISyntaxException {

        Output updatedOutput = repository.findById(id)
                .map(output -> {
                    BeanUtils.copyProperties(newOutput, output, "outputId, creationDate, type, gpio");

                    getDataService(output.getType()).update(
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

    @DeleteMapping(value = {"/{id}"}, produces = {"application/hal+json"})
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        getDataService(repository.findById(id)
                .orElseThrow(() -> new OutputNotFoundException(id))
                .getType()).delete(id);

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    /* ****************************************************
     ********************** CONTROL ************************
     **************************************************** */

    // ****************** PWM ************************
    @GetMapping(value = {"/control/pwm"}, produces = {"application/hal+json"})
    public Resource<PwmSignal> getPwmSignal(@RequestParam(name = "id") Integer id) {

        Output output = repository.findById(id).orElseThrow(() -> new OutputNotFoundException(id));

        return pwmSignalAssembler.toResource(pwmService.getSignal(output.getOutputId(), output.getReverse()));
    }

    @PutMapping(value = {"/control/pwm"}, produces = {"application/hal+json"})
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

    @GetMapping(value = {"/control/digital"}, produces = {"application/hal+json"})
    public Resource<DigitalState> getState(@RequestParam(name = "id") Integer id) {

        Output output = repository.findById(id).orElseThrow(() -> new OutputNotFoundException(id));

        return digitalStateAssembler.toResource(digitalService.getState(output.getOutputId(), output.getReverse()));
    }

    @PutMapping(value = {"/control/digital"}, produces = {"application/hal+json"})
    public ResponseEntity<?> setState(@RequestBody DigitalState state) throws URISyntaxException {

        Output output = repository.findById(state.getOutputId())
                .orElseThrow(() -> new OutputNotFoundException(state.getOutputId()));

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
                throw new TypeNotFoundException(type);
        }
    }
}