package tk.hiddenname.smarthome.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entities.DigitalOutput;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;
import tk.hiddenname.smarthome.repository.DigitalOutputsRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/outputs/digital")
class DigitalOutputRestController {

    private final String TYPE = "digital";
    private final DigitalOutputsRepository repository;
    private final DigitalOutputResourceAssembler assembler;

    DigitalOutputRestController(DigitalOutputsRepository repository,
                                DigitalOutputResourceAssembler assembler) {

        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping
    Resources<Resource<DigitalOutput>> getAll() {

        List<Resource<DigitalOutput>> outputs = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(outputs,
                linkTo(methodOn(DigitalOutputRestController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    Resource<DigitalOutput> getOne(@PathVariable Integer id) {

        return assembler.toResource(
                repository.findById(id)
                        .orElseThrow(() -> new OutputNotFoundException(TYPE, id)));
    }

    @PostMapping
    ResponseEntity<?> create(@RequestBody DigitalOutput newOutput) throws URISyntaxException {

        Resource<DigitalOutput> resource = assembler.toResource(repository.save(newOutput));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replace(@RequestBody DigitalOutput newOutput,
                              @PathVariable Integer id) throws URISyntaxException {

        DigitalOutput updatedOutput = repository.findById(id)
                .map(output -> {
                    BeanUtils.copyProperties(newOutput, output, "id");
                    return repository.save(output);
                })
                .orElseGet(() -> {
                    newOutput.setId(id);
                    return repository.save(newOutput);
                });

        Resource<DigitalOutput> resource = assembler.toResource(updatedOutput);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Integer id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}