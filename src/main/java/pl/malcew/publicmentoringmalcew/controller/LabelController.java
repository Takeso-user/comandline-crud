package pl.malcew.publicmentoringmalcew.controller;

import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.service.LabelService;
import pl.malcew.publicmentoringmalcew.view.LabelView;

import java.util.List;

@Component
public class LabelController {
    private final LabelView labelView;
    private final LabelService labelService;

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
                Long id= getLabelId();
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

    private void viewAllLabels() {
        labelView.displayLabels(labelService.viewAllLabels());
    }

    private void deleteLabel() {
        Long id = labelView.provideLabelId();
        labelService.deleteLabel(id);
    }

    private void updateLabel() {
        Long id = labelView.provideLabelId();
        String name = labelView.provideLabelName();
        Label label = new Label(id, name);
        labelService.updateLabel(label);
    }

    private void readLabel() {
        Long id = labelView.provideLabelId();
        Label label = labelService.readLabel(id);
        if(label == null) {
            System.out.println("Label not found");
            return;
        }
        labelView.displayLabels(List.of(label));
    }

    private Long getLabelId() {
        return labelView.provideLabelId();
    }

    private void createLabel() {
        String labelName = labelView.provideLabelName();
        labelService.createLabel(labelName);
    }
}
