package tk.hiddenname.smarthome;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import tk.hiddenname.smarthome.entity.Output;
import tk.hiddenname.smarthome.repository.OutputsRepository;
import tk.hiddenname.smarthome.service.digital.DigitalOutputServiceImpl;
import tk.hiddenname.smarthome.service.pwm.PwmOutputServiceImpl;
import tk.hiddenname.smarthome.utils.gpio.GPIO;

@SpringBootApplication
public class Application {

    private static final GpioController GPIO_FACTORY;

    static {
        GPIO.setPwmRange(1024);
        GPIO_FACTORY = GpioFactory.getInstance();
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Application.getGpioController().shutdown()));

        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        OutputsRepository repo = ctx.getBean(OutputsRepository.class);
        {
            // Digital outputs
            DigitalOutputServiceImpl service = ctx.getBean(DigitalOutputServiceImpl.class);

            for (Output output : repo.findByType("digital")) {
                service.getMap().put(output.getOutputId(), GPIO.createDigitalPin(
                        output.getGpio(),
                        output.getName(),
                        output.getReverse()
                ));
            }
        }
        {
            // Pwm outputs
            PwmOutputServiceImpl service = ctx.getBean(PwmOutputServiceImpl.class);

            for (Output output : repo.findByType("pwm")) {
                service.getMap().put(output.getOutputId(), GPIO.createPwmPin(
                        output.getGpio(),
                        output.getName(),
                        output.getReverse()
                ));
            }
        }
    }


    public static GpioController getGpioController() {
        return GPIO_FACTORY;
    }
}
