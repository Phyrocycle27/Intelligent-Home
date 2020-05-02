package tk.hiddenname.smarthome;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.hardware.Sensor;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.netty.Client;
import tk.hiddenname.smarthome.repository.DeviceRepository;
import tk.hiddenname.smarthome.repository.SensorRepository;
import tk.hiddenname.smarthome.service.manager.DeviceManager;
import tk.hiddenname.smarthome.service.manager.SensorManager;
import tk.hiddenname.smarthome.utils.gpio.GPIOManager;

@SpringBootApplication
public class Application {

    private static final Logger log;
    private static final String HOST = "87.255.8.24";
    private static final int PORT = 3141;

    @Getter
    private static final GpioController gpioController;
    // Settings
    @Getter
    private static final boolean allowConnectionToServer = true;
    @Getter
    private static final String token = "Dq62R5gswBxAOqUITvO0KEH6aZmT6BQ8";

    static {
        GPIOManager.setPwmRange(1024);
        gpioController = GpioFactory.getInstance();
        log = LoggerFactory.getLogger(Application.class.getName());
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                Application.getGpioController().shutdown()));

        // Run the Spring
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        // Run the Netty
        if (allowConnectionToServer)
            new Client(HOST, PORT);

        // Creating outputs which exited in DB
        DeviceRepository deviceRepo = ctx.getBean(DeviceRepository.class);
        DeviceManager deviceManager = ctx.getBean(DeviceManager.class);

        for (Device device : deviceRepo.findAll()) {
            try {
                deviceManager.create(device);
            } catch (PinSignalSupportException | GPIOBusyException e) {
                log.warn(e.getMessage());
            }
        }

        SensorRepository sensorRepo = ctx.getBean(SensorRepository.class);
        SensorManager sensorManager = ctx.getBean(SensorManager.class);

        for (Sensor sensor : sensorRepo.findAll()) {
            try {
                sensorManager.create(sensor);
            } catch (PinSignalSupportException | GPIOBusyException e) {
                log.warn(e.getMessage());
            }
        }
    }
}
