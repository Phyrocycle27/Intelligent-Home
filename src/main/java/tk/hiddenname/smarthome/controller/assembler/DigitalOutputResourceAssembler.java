package tk.hiddenname.smarthome.controller.assembler;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.controller.DigitalOutputRestController;
import tk.hiddenname.smarthome.entity.output.DigitalOutput;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class DigitalOutputResourceAssembler implements ResourceAssembler<DigitalOutput, Resource<DigitalOutput>> {

    @Override
    public Resource<DigitalOutput> toResource(DigitalOutput output) {

        return new Resource<>(output,
                ControllerLinkBuilder.linkTo(methodOn(DigitalOutputRestController.class).getOne(output.getId())).withSelfRel(),
                linkTo(methodOn(DigitalOutputRestController.class).getAll()).withRel("outputs"));
    }
}
