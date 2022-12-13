package sectrans.manipulador.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class SectransManipuladorApplication {

    public static void main(String[] args) {

        SpringApplication.run(SectransManipuladorApplication.class, args);

    }
}
