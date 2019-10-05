package tk.hiddenname.smarthome.controller.assembler;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.controller.AnalogOutputRestController;
import tk.hiddenname.smarthome.entity.output.AnalogOutput;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class AnalogOutputResourceAssembler implements ResourceAssembler<AnalogOutput, Resource<AnalogOutput>> {

    @Override
    public Resource<AnalogOutput> toResource(AnalogOutput output) {

        return new Resource<>(output,
                ControllerLinkBuilder.linkTo(methodOn(AnalogOutputRestController.class).getOne(output.getId())).withSelfRel(),
                linkTo(methodOn(AnalogOutputRestController.class).getAll()).withRel("outputs"));
    }
}
