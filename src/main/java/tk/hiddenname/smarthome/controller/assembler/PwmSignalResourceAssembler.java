package tk.hiddenname.smarthome.controller.assembler;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.controller.OutputRestController;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class PwmSignalResourceAssembler implements ResourceAssembler<PwmSignal, Resource<PwmSignal>> {

    @Override
    public Resource<PwmSignal> toResource(PwmSignal pwmSignal) {

        return new Resource<>(pwmSignal,
                linkTo(methodOn(OutputRestController.class).getPwmSignal(pwmSignal.getOutputId())).withSelfRel());
    }
}
