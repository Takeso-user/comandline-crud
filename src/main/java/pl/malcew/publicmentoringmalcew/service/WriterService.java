package pl.malcew.publicmentoringmalcew.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.repo.WriterRepo;

import java.util.List;

@Component
public class WriterService {
    private final WriterRepo writerRepo;
    private static final Logger LOGGER = LoggerFactory.getLogger(WriterService.class);

    public WriterService(WriterRepo writerRepo) {
        this.writerRepo = writerRepo;
    }

    public Long createWriter(Writer writer) {
        LOGGER.info("Creating writer: {}", writer);
        Long id = writerRepo.create(writer);
        LOGGER.info("Writer created with id: {}", id);
        return id;
    }

    public Writer readWriter(Long id) {
        LOGGER.info("Reading writer with id: {}", id);
        Writer writer = writerRepo.read(id);
        LOGGER.info("Writer read: {}", writer);
        return writer;
    }

    public Writer updateWriter(Writer writer) {
        LOGGER.info("Updating writer: {}", writer);
        Writer updatedWriter = writerRepo.update(writer);
        LOGGER.info("Writer updated: {}", updatedWriter);
        return updatedWriter;
    }

    public Long deleteWriter(Writer writer) {
        LOGGER.info("Deleting writer: {}", writer);
        try {
            Long id = writerRepo.delete(writer);
            LOGGER.info("Writer deleted with id: {}", id);
            return id;
        } catch (Exception e) {
            LOGGER.error("Error deleting writer: ", e);
        }
        return 0L;
    }

    public List<Writer> viewAllWriters() {
        LOGGER.info("Viewing all writers");
        List<Writer> writers = writerRepo.viewAll();
        LOGGER.info("Writers viewed: {}", writers);
        return writers;
    }
}