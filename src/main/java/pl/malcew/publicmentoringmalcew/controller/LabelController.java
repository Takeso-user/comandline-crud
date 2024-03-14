package pl.malcew.publicmentoringmalcew.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.service.LabelService;
import pl.malcew.publicmentoringmalcew.view.LabelView;

import java.util.List;

@Component
public class LabelController {
    private final LabelView labelView;
    private final LabelService labelService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LabelController.class);

    public LabelController(LabelView labelView, LabelService labelService) {
        this.labelView = labelView;
        this.labelService = labelService;
    }


    public void handleMenu() {
        labelView.displayMenu();
        int choice = labelView.readOption();
        switch (choice) {
            case 1:
                createLabel();
                break;
            case 2:
                readLabel();
                break;
            case 3:
                updateLabel();
                break;
            case 4:
                deleteLabel();
                break;
            case 5:
                viewAllLabels();
                break;
            case 0:
                return;
            default:
                System.err.println(("Invalid choice"));
        }
    }

    void viewAllLabels() {
        LOGGER.info("Viewing all labels");
        labelView.displayLabels(labelService.viewAllLabels());
    }

    void deleteLabel() {
        Long id = labelView.provideLabelId();
        LOGGER.info("Deleting label with id: " + labelService.deleteLabel(id));
    }

    void updateLabel() {
        Long id = labelView.provideLabelId();
        String name = labelView.provideLabelName();
        Label label = new Label(id, name);
        LOGGER.info("Updating label with id: " + id);
        LOGGER.info("New label : " + labelService.updateLabel(label));
    }

    void readLabel() {
        Long id = labelView.provideLabelId();
        Label label = labelService.readLabel(id);
        LOGGER.info("Reading label with id: " + id);
        LOGGER.info("Label : " + label);
        if(label == null) {
            System.out.println("Label not found");
            return;
        }
        labelView.displayLabels(List.of(label));
    }

    void createLabel() {
        String labelName = labelView.provideLabelName();
        LOGGER.info("Creating label with name: " + labelName + " and id: " + labelService.createLabel(labelName));
    }
}
