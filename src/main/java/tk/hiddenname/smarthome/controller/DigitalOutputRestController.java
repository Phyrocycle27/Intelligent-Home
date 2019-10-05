package tk.hiddenname.smarthome.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.controller.assembler.DigitalOutputResourceAssembler;
import tk.hiddenname.smarthome.controller.assembler.StateResourceAssembler;
import tk.hiddenname.smarthome.entity.State;
import tk.hiddenname.smarthome.entity.output.DigitalOutput;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.repository.DigitalOutputsRepository;
import tk.hiddenname.smarthome.service.DigitalOutputService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/outputs/digital")
public class DigitalOutputRestController {

    public static final String TYPE = "digital"; // Type of output which support the controller
    private final DigitalOutputsRepository repository; // repository which connects postgresDB to our program
    private final DigitalOutputResourceAssembler assembler;
    private final DigitalOutputService service;
    private final StateResourceAssembler stateAssembler;

    public DigitalOutputRestController(DigitalOutputsRepository repository, DigitalOutputService service,
                                       DigitalOutputResourceAssembler assembler, StateResourceAssembler stateAssembler) {

        this.repository = repository;
        this.service = service;
        this.assembler = assembler;
        this.stateAssembler = stateAssembler;
    }

    @GetMapping
    public Resources<Resource<DigitalOutput>> getAll() {

        List<Resource<DigitalOutput>> outputs = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(outputs,
                linkTo(methodOn(DigitalOutputRestController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public Resource<DigitalOutput> getOne(@PathVariable Integer id) {

        return assembler.toResource(
                repository.findById(id)
                        .orElseThrow(() -> new OutputNotFoundException(TYPE, id)));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DigitalOutput newOutput) throws URISyntaxException {

        Resource<DigitalOutput> resource = assembler.toResource(repository.save(newOutput));
        service.save(newOutput);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@RequestBody DigitalOutput newOutput,
                                     @PathVariable Integer id) throws URISyntaxException {

        DigitalOutput updatedOutput = repository.findById(id)
                .map(output -> {
                    BeanUtils.copyProperties(newOutput, output, "id");
                    service.update(newOutput, id);
                    return repository.save(output);
                })
                .orElseGet(() -> {
                    newOutput.setId(id);
                    service.save(newOutput);
                    return repository.save(newOutput);
                });
        Resource<DigitalOutput> resource = assembler.toResource(updatedOutput);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        repository.deleteById(id);
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    // Work with state

    @GetMapping("/{id}/state")
    public Resource<State> getState(@PathVariable Integer outputId) {
        return stateAssembler.toResource(service.getState(outputId));
    }

    @PutMapping("/{id}/")
    public ResponseEntity<?> setState(
            @PathVariable Integer id, @RequestParam Boolean newState) throws URISyntaxException {

        Resource<State> resource = stateAssembler.toResource(service.setState(id, newState));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
}