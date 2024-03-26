package pl.malcew.publicmentoringmalcew;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.malcew.publicmentoringmalcew.model.Label;

import java.util.ArrayList;
import java.util.Comparator;

@SpringBootApplication
public class PublicMentoringMalcewApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PublicMentoringMalcewApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
