package tk.hiddenname.smarthome.controller.assembler;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.controller.OutputRestController;
import tk.hiddenname.smarthome.entity.signal.DigitalState;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class DigitalStateResourceAssembler implements ResourceAssembler<DigitalState, Resource<DigitalState>> {

    @Override
    public Resource<DigitalState> toResource(DigitalState state) {

        return new Resource<>(state,
                linkTo(methodOn(OutputRestController.class).getState(state.getOutputId())).withSelfRel());
    }
}
