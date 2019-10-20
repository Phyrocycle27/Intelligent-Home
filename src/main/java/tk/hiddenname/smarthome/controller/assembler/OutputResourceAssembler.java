package tk.hiddenname.smarthome.controller.assembler;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.controller.OutputRestController;
import tk.hiddenname.smarthome.entity.Output;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class OutputResourceAssembler implements ResourceAssembler<Output, Resource<Output>> {

    @Override
    public Resource<Output> toResource(Output output) {

        return new Resource<>(output,
                linkTo(methodOn(OutputRestController.class).getOne(output.getOutputId())).withSelfRel(),
                linkTo(methodOn(OutputRestController.class).getAll(output.getType())).withRel("outputs"));
    }
}
