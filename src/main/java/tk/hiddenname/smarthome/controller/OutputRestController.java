package tk.hiddenname.smarthome.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
public class OutputRestController {

    private final OutputsRepository repository; // repository which connects postgresDB to our program
    private final OutputResourceAssembler assembler;

    // services
    private final DigitalOutputServiceImpl digitalService;
    private final PwmOutputServiceImpl pwmService;

    // State and signal resource assemblers
    private final DigitalStateResourceAssembler digitalStateAssembler;
    private final PwmSignalResourceAssembler pwmSignalAssembler;

    @Autowired
    public OutputRestController(OutputsRepository repository, OutputResourceAssembler assembler,
                                DigitalOutputServiceImpl digitalService, PwmOutputServiceImpl pwmService,
                                DigitalStateResourceAssembler digitalStateAssembler,
                                PwmSignalResourceAssembler pwmSignalAssembler) {

        this.repository = repository;
        this.assembler = assembler;

        this.digitalService = digitalService;
        this.pwmService = pwmService;

        this.digitalStateAssembler = digitalStateAssembler;
        this.pwmSignalAssembler = pwmSignalAssembler;
    }

    @GetMapping(produces = {"application/hal+json"})
    public Resources<Resource<Output>> getAll(
            @RequestParam(name = "type", defaultValue = "", required = false) String type) {

        type = type.toLowerCase();
        List<Resource<Output>> outputs;

        if (type.equals("")) {
            outputs = repository.findAll(Sort.by("outputId")).stream()
                    .map(assembler::toResource)
                    .collect(Collectors.toList());
        } else if (type.equals("pwm") | type.equals("digital")){
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


        getDataService(newOutput.getType()).save(newOutput);
            Resource<Output> resource = assembler.toResource(repository.save(newOutput));

        System.out.println("\nCREATE: \n\tNew output is: " + newOutput);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping(value = {"/{id}"}, produces = {"application/hal+json"})
    public ResponseEntity<?> replace(@RequestBody Output newOutput,
                                     @PathVariable Integer id) throws URISyntaxException {

        System.out.println("\nPUT:");

        Output updatedOutput = repository.findById(id)
                .map(output -> {
                    /*System.out.println("\tNew output: "+ newOutput);
                    System.out.println("\tExisting output (before update): " + output);*/
                    output.setName(newOutput.getName());
                    output.setReverse(newOutput.getReverse());
                    //System.out.println("\tExisting output (after update): " + output);
                    getDataService(output.getType()).update(output);
                    return repository.save(output);
                })
                .orElseThrow(() -> new OutputNotFoundException(id)

                    /*newOutput.setOutputId(id);
                    newOutput.setCreationDate(LocalDateTime.now());
                    System.out.println("New output: "+ newOutput);
                    getDataService(newOutput.getType()).save(newOutput);
                    return repository.save(newOutput);*/
                );

        System.out.println("Updated output is: " + updatedOutput);
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

        return pwmSignalAssembler.toResource(pwmService.getSignal(repository.findById(id)
                .orElseThrow(() -> new OutputNotFoundException(id))));
    }

    @PutMapping(value = {"/control/pwm"}, produces = {"application/hal+json"})
    public ResponseEntity<?> setPwmSignal(@RequestBody PwmSignal signal) throws URISyntaxException {

        Resource<PwmSignal> resource = pwmSignalAssembler.toResource(pwmService.setSignal(
                repository.findById(signal.getOutputId())
                        .orElseThrow(() -> new OutputNotFoundException(signal.getOutputId())),
                signal.getPwmSignal()));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    // ********************************************************

    // ******************** DIGITAL *******************************

    @GetMapping(value = {"/control/digital"}, produces = {"application/hal+json"})
    public Resource<DigitalState> getState(@RequestParam(name = "id") Integer id) {

        return digitalStateAssembler.toResource(digitalService.getState(repository.findById(id)
                .orElseThrow(() -> new OutputNotFoundException(id))));
    }

    @PutMapping(value = {"/control/digital"}, produces = {"application/hal+json"})
    public ResponseEntity<?> setState(@RequestBody DigitalState state) throws URISyntaxException {

        Resource<DigitalState> resource = digitalStateAssembler.toResource(digitalService.setState(
                repository.findById(state.getOutputId())
                        .orElseThrow(() -> new OutputNotFoundException(state.getOutputId())),
                state.getDigitalState()));

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