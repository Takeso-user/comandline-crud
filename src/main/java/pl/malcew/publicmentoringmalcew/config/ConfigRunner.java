package pl.malcew.publicmentoringmalcew.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.service.LabelService;
import pl.malcew.publicmentoringmalcew.service.PostService;
import pl.malcew.publicmentoringmalcew.service.WriterService;
import pl.malcew.publicmentoringmalcew.view.Bootstrap;
@Component
public class ConfigRunner {

    private final WriterService writerService;
    private final LabelService labelService;
    private final PostService postService;

    public ConfigRunner(WriterService writerService, LabelService labelService, PostService postService) {
        this.writerService = writerService;
        this.labelService = labelService;
        this.postService = postService;
    }
    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("!!!Hello, Writer App!!!");

            Bootstrap bootstrap = new Bootstrap(writerService, labelService, postService);
            bootstrap.run();

        };
    }
}
