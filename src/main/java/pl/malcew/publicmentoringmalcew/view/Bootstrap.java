package pl.malcew.publicmentoringmalcew.view;


import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.controller.LabelController;
import pl.malcew.publicmentoringmalcew.controller.PostController;
import pl.malcew.publicmentoringmalcew.controller.WriteController;
import pl.malcew.publicmentoringmalcew.service.LabelService;
import pl.malcew.publicmentoringmalcew.service.PostService;
import pl.malcew.publicmentoringmalcew.service.WriterService;

import java.util.Scanner;

@Component
public class Bootstrap {

    private final WriterService writerService;
    private final LabelService labelService;
    private final PostService postService;

    private final WriteController writeController;
    private final LabelController labelController;
    private final PostController postController;


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
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Select an option:");
            System.out.println("1. Writer operations");
            System.out.println("2. Label operations");
            System.out.println("3. Post operations");
            System.out.println("4. Exit");

            System.out.print("Enter option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    writeController.handleMenu();
                    break;
                case 2:
                    labelController.handleMenu();
                    break;
                case 3:
                    postController.handleMenu();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}