package tk.hiddenname.smarthome;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import tk.hiddenname.smarthome.entity.Output;
import tk.hiddenname.smarthome.exception.OutputAlreadyExistException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.netty.Client;
import tk.hiddenname.smarthome.repository.OutputsRepository;
import tk.hiddenname.smarthome.utils.gpio.GPIO;
import tk.hiddenname.smarthome.utils.gpio.OutputManager;

@SpringBootApplication
public class Application {

    private static final Logger log;
    private static final String HOST = "192.168.1.54";
    private static final int PORT = 3141;

    @Getter
    private static final GpioController gpioController;
    // Settings
    @Getter
    private static final boolean allowConnectionToServer = false;
    @Getter
    private static final String token = "ebhtiWP9FEwQCKxVvCRPepawT88kPYa8";
    //private static final String token = "ag6EBKHA77Tlp1bluQdwAUe8EbxGyeh6";

    static {
        GPIO.setPwmRange(1024);
        gpioController = GpioFactory.getInstance();

        log = LoggerFactory.getLogger(Application.class.getName());
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                Application.getGpioController().shutdown())
        );

        // Run the Spring
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        // Run the Netty
        if (allowConnectionToServer)
            new Client(HOST, PORT);

        // Creating outputs which exited in DB
        OutputsRepository repository = ctx.getBean(OutputsRepository.class);
        OutputManager manager = ctx.getBean(OutputManager.class);

        for (Output output : repository.findAll()) {
            try {
                manager.create(output);
            } catch (PinSignalSupportException | OutputAlreadyExistException e) {
                e.printStackTrace();
            }
        }
    }
}
