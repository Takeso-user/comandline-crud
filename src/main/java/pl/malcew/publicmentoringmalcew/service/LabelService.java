package pl.malcew.publicmentoringmalcew.service;

import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.repo.LabelRepo;

import java.util.List;

@Component
public class LabelService {
    private final LabelRepo labelRepo;

    public LabelService(LabelRepo labelRepo) {
        this.labelRepo = labelRepo;
    }

    public void createLabel(String name) {
        labelRepo.create(new Label(null, name));
    }

    public Label readLabel(Long id) {
       return labelRepo.read(id);
    }

    public void updateLabel(Label label) {
        labelRepo.update(label);
    }

    public void deleteLabel(Long id) {
        Label label = labelRepo.read(id);
        labelRepo.delete(label);
    }

    public List<Label> viewAllLabels() {
        return labelRepo.viewAll();
    }
}
