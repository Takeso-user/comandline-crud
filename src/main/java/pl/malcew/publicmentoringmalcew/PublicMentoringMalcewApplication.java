package pl.malcew.publicmentoringmalcew;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.malcew.publicmentoringmalcew.repo.LabelRepo;
import pl.malcew.publicmentoringmalcew.repo.PostRepo;
import pl.malcew.publicmentoringmalcew.repo.WriterRepo;
import pl.malcew.publicmentoringmalcew.service.LabelService;
import pl.malcew.publicmentoringmalcew.service.PostService;
import pl.malcew.publicmentoringmalcew.service.WriterService;
import pl.malcew.publicmentoringmalcew.view.Bootstrap;

@SpringBootApplication
public class PublicMentoringMalcewApplication {

    private final WriterRepo writerRepo;
    private final LabelRepo labelRepo;
    private final PostRepo postRepo;

    public PublicMentoringMalcewApplication(WriterRepo writerRepo, LabelRepo labelRepo, PostRepo postRepo) {
        this.writerRepo = writerRepo;
        this.labelRepo = labelRepo;
        this.postRepo = postRepo;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PublicMentoringMalcewApplication.class);
        // Modify the application configuration
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            // This code will run when the application starts
            System.out.println("!!!Hello, Writer App!!!");

            WriterService writerService = new WriterService(writerRepo);
            LabelService labelService = new LabelService(labelRepo);
            PostService postService = new PostService(postRepo);

            Bootstrap bootstrap = new Bootstrap(writerService, labelService, postService);
            bootstrap.run();

        };
    }
}
