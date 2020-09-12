package tk.hiddenname.smarthome;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import tk.hiddenname.smarthome.netty.Client;
import tk.hiddenname.smarthome.service.hardware.manager.DeviceManager;
import tk.hiddenname.smarthome.service.hardware.manager.SensorManager;
import tk.hiddenname.smarthome.service.task.TaskManager;
import tk.hiddenname.smarthome.utils.gpio.GpioManager;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static final String HOST = "87.255.8.24";
    private static final int PORT = 3141;


    private static final boolean allowConnectionToServer = true;
    @Getter
    private static final String token = "Dq62R5gswBxAOqUITvO0KEH6aZmT6BQ8";

    public static void main(String[] args) {
        // Run the Spring
        ApplicationContext ctx = SpringApplication.run(Application.class);

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                ctx.getBean(GpioManager.class).shutdown()));

        // Run the Netty
        if (allowConnectionToServer)
            new Client(HOST, PORT);

        ctx.getBean(DeviceManager.class).loadDevices();
        ctx.getBean(SensorManager.class).loadSensors();

        ctx.getBean(TaskManager.class).loadTasks();
    }
}
