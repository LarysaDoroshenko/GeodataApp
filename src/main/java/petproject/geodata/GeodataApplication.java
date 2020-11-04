package petproject.geodata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeodataApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeodataApplication.class, args);
    }

}
