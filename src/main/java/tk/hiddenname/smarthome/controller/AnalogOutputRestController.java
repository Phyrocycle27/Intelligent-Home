package tk.hiddenname.smarthome.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.controller.assembler.AnalogOutputResourceAssembler;
import tk.hiddenname.smarthome.entity.output.AnalogOutput;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.repository.AnalogOutputsRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/outputs/analog")
public class AnalogOutputRestController {

    private final String TYPE = "digital"; // Type of output which support the controller
    private final AnalogOutputsRepository repository; // repository which connects postgresDB to our program
    private final AnalogOutputResourceAssembler assembler;

    public AnalogOutputRestController(AnalogOutputsRepository repository,
                                      AnalogOutputResourceAssembler assembler) {

        this.repository = repository;
        this.assembler = assembler;
    }

    /* ********* "GET ALL" METHOD ***************** */
    @GetMapping
    public Resources<Resource<AnalogOutput>> getAll() {

        List<Resource<AnalogOutput>> outputs = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(outputs,
                linkTo(methodOn(AnalogOutputRestController.class).getAll()).withSelfRel());
    }

    /* ************* "GET ONE BY ID" METHOD ********************* */
    @GetMapping("/{id}")
    public Resource<AnalogOutput> getOne(@PathVariable Integer id) {

        return assembler.toResource(
                repository.findById(id)
                        .orElseThrow(() -> new OutputNotFoundException(TYPE, id)));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AnalogOutput newOutput) throws URISyntaxException {

        Resource<AnalogOutput> resource = assembler.toResource(repository.save(newOutput));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@RequestBody AnalogOutput newOutput,
                                     @PathVariable Integer id) throws URISyntaxException {

        AnalogOutput updatedOutput = repository.findById(id)
                .map(output -> {
                    BeanUtils.copyProperties(newOutput, output, "id");
                    return repository.save(output);
                })
                .orElseGet(() -> {
                    newOutput.setId(id);
                    return repository.save(newOutput);
                });

        Resource<AnalogOutput> resource = assembler.toResource(updatedOutput);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
