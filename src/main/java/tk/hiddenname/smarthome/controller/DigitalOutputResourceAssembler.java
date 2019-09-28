package tk.hiddenname.smarthome.controller;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entities.DigitalOutput;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class DigitalOutputResourceAssembler implements ResourceAssembler<DigitalOutput, Resource<DigitalOutput>> {

    @Override
    public Resource<DigitalOutput> toResource(DigitalOutput output) {

        return new Resource<>(output,
                linkTo(methodOn(DigitalOutputRestController.class).getOne(output.getId())).withSelfRel(),
                linkTo(methodOn(DigitalOutputRestController.class).getAll()).withRel("outputs"));
    }
}
