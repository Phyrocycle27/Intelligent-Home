package tk.hiddenname.smarthome;

import com.pi4j.io.gpio.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import tk.hiddenname.smarthome.entities.Control;
import tk.hiddenname.smarthome.gpio.GPIOCreator;
import tk.hiddenname.smarthome.repository.ControlRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SmartHomeApplication {

    private List<GpioPinDigitalOutput> outputs;
    private List<GpioPinDigitalInput> inputs;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SmartHomeApplication.class, args);

        ControlRepository controlRepo = context.getBean(ControlRepository.class);

        GpioController controller = GpioFactory.getInstance();
        GPIOCreator creator = new GPIOCreator();
        List<GpioPinDigitalOutput> outputs = new ArrayList<>();

        if (!controlRepo.findAll().isEmpty()) {
            for (Control control : controlRepo.findAll()) {
                System.out.println("Control is: " + control.toString());
                outputs.add(creator.getGPIO(control.getGpioNumber(), control.getName(), control.getState()));
            }
        } else System.out.println("list is empty");
        System.out.println(outputs.toString());
    }

}
