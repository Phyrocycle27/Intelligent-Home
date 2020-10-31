package tk.hiddenname.smarthome

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import tk.hiddenname.smarthome.netty.Client
import tk.hiddenname.smarthome.service.hardware.manager.DeviceManager
import tk.hiddenname.smarthome.service.hardware.manager.SensorManager
import tk.hiddenname.smarthome.service.task.TaskService
import tk.hiddenname.smarthome.utils.gpio.GpioManager

@SpringBootApplication
open class Application {
    companion object {
        private const val HOST = "87.255.8.24"
        private const val PORT = 3141
        private const val allowConnectionToServer = true

        const val token = "Dq62R5gswBxAOqUITvO0KEH6aZmT6BQ8"

        @JvmStatic
        fun main(args: Array<String>) {
            // Run the Spring
            val ctx: ApplicationContext = SpringApplication.run(Application::class.java)
            Runtime.getRuntime().addShutdownHook(Thread { ctx.getBean(GpioManager::class.java).shutdown() })

            // Run the Netty
            if (allowConnectionToServer) {
                Client(HOST, PORT)
            }

            ctx.getBean(DeviceManager::class.java).loadDevices()
            ctx.getBean(SensorManager::class.java).loadSensors()
            ctx.getBean(TaskService::class.java).loadTasks()
        }
    }
}