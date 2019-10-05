package tk.hiddenname.smarthome.controller.assembler;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.controller.DigitalOutputRestController;
import tk.hiddenname.smarthome.entity.State;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class StateResourceAssembler implements ResourceAssembler<State, Resource<State>> {

    @Override
    public Resource<State> toResource(State state) {

        return new Resource<>(state,
                linkTo(methodOn(DigitalOutputRestController.class).getState(state.getOutputId())).withSelfRel());
    }
}
