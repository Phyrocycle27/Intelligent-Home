package tk.hiddenname.smarthome;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import tk.hiddenname.smarthome.entity.Output;
import tk.hiddenname.smarthome.exception.OutputAlreadyExistException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.netty.Client;
import tk.hiddenname.smarthome.repository.OutputsRepository;
import tk.hiddenname.smarthome.service.digital.DigitalOutputServiceImpl;
import tk.hiddenname.smarthome.service.pwm.PwmOutputServiceImpl;
import tk.hiddenname.smarthome.utils.gpio.GPIO;

import java.util.logging.Logger;

@SpringBootApplication
public class Application {

    private static final Logger LOGGER;
    private static final String HOST = "192.168.1.54";
    private static final int PORT = 3141;

    @Getter
    private static final GpioController gpioController;
    // Settings
    @Getter
    private static final boolean allowConnectionToServer = true;
    @Getter
    private static final String token = "ebhtiWP9FEwQCKxVvCRPepawT88kPYa8";
    //private static final String token = "ag6EBKHA77Tlp1bluQdwAUe8EbxGyeh6";

    static {
        GPIO.setPwmRange(1024);
        gpioController = GpioFactory.getInstance();

        LOGGER = Logger.getLogger(Application.class.getName());
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Application.getGpioController().shutdown()));

        // Run the Spring
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        // Run the Netty
        new Client(HOST, PORT);

        // Creating outputs
        OutputsRepository repo = ctx.getBean(OutputsRepository.class);
        {
            DigitalOutputServiceImpl service = ctx.getBean(DigitalOutputServiceImpl.class);

            for (Output output : repo.findByType("digital")) {
                try {
                    service.getMap().put(output.getOutputId(), GPIO.createDigitalPin(
                            output.getGpio(),
                            output.getName(),
                            output.getReverse()
                    ));
                } catch (OutputAlreadyExistException | PinSignalSupportException e) {
                    e.printStackTrace();
                }
            }
        }
        {
            PwmOutputServiceImpl service = ctx.getBean(PwmOutputServiceImpl.class);

            for (Output output : repo.findByType("pwm")) {
                try {
                    service.getMap().put(output.getOutputId(), GPIO.createPwmPin(
                            output.getGpio(),
                            output.getName(),
                            output.getReverse()
                    ));
                } catch (OutputAlreadyExistException | PinSignalSupportException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
