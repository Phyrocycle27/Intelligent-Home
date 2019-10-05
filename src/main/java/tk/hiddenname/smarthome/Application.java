package tk.hiddenname.smarthome;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import tk.hiddenname.smarthome.entity.output.DigitalOutput;
import tk.hiddenname.smarthome.repository.DigitalOutputsRepository;
import tk.hiddenname.smarthome.service.DigitalOutputServiceImpl;
import tk.hiddenname.smarthome.utils.gpio.GPIO;

@SpringBootApplication
public class Application {

    private static final GpioController GPIO_FACTORY = GpioFactory.getInstance();

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx =
                SpringApplication.run(Application.class, args);

        Gpio.wiringPiSetup();

        // Digital outputs
        {
            DigitalOutputsRepository repo = ctx.getBean(DigitalOutputsRepository.class);
            DigitalOutputServiceImpl service = ctx.getBean(DigitalOutputServiceImpl.class);

            for (DigitalOutput output : repo.findAll()) {
                service.getMap().put(output.getId(), GPIO.convert(output));
            }
        }
    }

    public static GpioController getGpioFactory() {
        return GPIO_FACTORY;
    }
}
