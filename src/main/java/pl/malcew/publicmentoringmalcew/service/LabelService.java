package pl.malcew.publicmentoringmalcew.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.repo.LabelRepo;

import java.util.List;

@Component
public class LabelService {
    private final LabelRepo labelRepo;
    private static final Logger LOGGER = LoggerFactory.getLogger(LabelService.class);

    public LabelService(LabelRepo labelRepo) {
        this.labelRepo = labelRepo;
    }

    public Long createLabel(String name) {
        LOGGER.info("Creating label with name: {}", name);
        Long id = labelRepo.create(new Label(null, name));
        LOGGER.info("Label created with id: {}", id);
        return id;
    }

    public Label readLabel(Long id) {
        LOGGER.info("Reading label with id: {}", id);
        Label label = labelRepo.read(id);
        LOGGER.info("Label read: {}", label);
        return label;
    }

    public Label updateLabel(Label label) {
        LOGGER.info("Updating label: {}", label);
        Label updatedLabel = labelRepo.update(label);
        LOGGER.info("Label updated: {}", updatedLabel);
        return updatedLabel;
    }

    public Long deleteLabel(Long id) {
        LOGGER.info("Deleting label with id: {}", id);
        var label = labelRepo.read(id);
        try {
            Long deletedId = labelRepo.delete(label);
            LOGGER.error("Label deleted with id: {}", deletedId);
            return deletedId;
        } catch (Exception e) {
            LOGGER.error("Error deleting label: ", e);
        }
        return 0L;
    }

    public List<Label> viewAllLabels() {
        LOGGER.info("Viewing all labels");
        List<Label> labels = labelRepo.viewAll();
        LOGGER.info("Labels viewed: {}", labels);
        return labels;
    }
}