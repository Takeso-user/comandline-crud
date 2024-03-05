package pl.malcew.publicmentoringmalcew.view;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.controller.LabelController;
import pl.malcew.publicmentoringmalcew.controller.PostController;
import pl.malcew.publicmentoringmalcew.controller.WriteController;
import pl.malcew.publicmentoringmalcew.service.LabelService;
import pl.malcew.publicmentoringmalcew.service.PostService;
import pl.malcew.publicmentoringmalcew.service.WriterService;

@Component
public class Bootstrap {

    private final WriterService writerService;
    private final LabelService labelService;
    private final PostService postService;

    private final WriteController writeController;
    private final LabelController labelController;
    private final PostController postController;

    @Autowired
    public Bootstrap(WriterService writerService, LabelService labelService, PostService postService) {
        this.writerService = writerService;
        this.labelService = labelService;
        this.postService = postService;

        WriterView writerView = new WriterView();
        LabelView labelView = new LabelView();
        PostView postView = new PostView();

        this.writeController = new WriteController(writerView, writerService);
        this.labelController = new LabelController(labelView, labelService);
        this.postController = new PostController(postView, postService, writerService, writerView);
    }

    public void run() {
        // Handle menu interactions for each entity
        writeController.handleMenu();
        labelController.handleMenu();
        postController.handleMenu();
    }
}