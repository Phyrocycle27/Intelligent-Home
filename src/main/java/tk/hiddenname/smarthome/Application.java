package tk.hiddenname.smarthome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import tk.hiddenname.smarthome.entities.Output;
import tk.hiddenname.smarthome.gpio.Outputs;
import tk.hiddenname.smarthome.repository.OutputRepository;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class);

        Outputs outputs = ctx.getBean(Outputs.class);
        OutputRepository repo = ctx.getBean(OutputRepository.class);

        System.out.println("----------------------------------------------------");
        for (Output output: repo.findAll()) {
            System.out.println(" (*) Output found: " + output.toString());
            outputs.add(output);
        }
    }
}
