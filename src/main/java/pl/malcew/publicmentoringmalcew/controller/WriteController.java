package pl.malcew.publicmentoringmalcew.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.model.WriterStatus;
import pl.malcew.publicmentoringmalcew.service.WriterService;
import pl.malcew.publicmentoringmalcew.view.WriterView;

import java.util.List;

@Component
public class WriteController {

    private final WriterView writerView;
    private final WriterService writerService;
    private static final Logger LOGGER = LoggerFactory.getLogger(WriteController.class);

    public WriteController(WriterView writerView, WriterService writerService) {
        this.writerView = writerView;
        this.writerService = writerService;
    }

    public void handleMenu() {
        while (true) {
            writerView.displayMenu();
            int option = writerView.readOption();
            switch (option) {
                case 1:
                    createWriter();
                    break;
                case 2:
                    updateWriter();
                    break;
                case 3:
                    deleteWriter();
                    break;
                case 4:
                    viewAllWriters();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private Long getWriterID() {
        var id = writerView.provideWriterId();
        LOGGER.info("Provide writer ID: {}", id);
        return id;
    }

    void createWriter() {
        Writer createWriter = writerView.readWriter();
        String firstName = createWriter.firstName();
        String lastName = createWriter.lastName();
        Writer wr = new Writer(null, firstName, lastName, null, WriterStatus.ACTIVE);
        var id = writerService.createWriter(wr);
        LOGGER.info("Writer {} with id {} created successfully", wr, id);
    }

    void updateWriter() {
        Writer existingWriter = writerService.readWriter(getWriterID());
        if (existingWriter == null) {
            LOGGER.error("No writer found with the provided ID.");
            return;
        }
        Writer updatedWriter = writerView.updateWriter(existingWriter);
        var writer = writerService.updateWriter(updatedWriter);
        LOGGER.info("Writer {} updated successfully", writer);
    }

    void deleteWriter() {
        Writer writerToDelete = writerService.readWriter(getWriterID());
        if (writerToDelete == null) {
            LOGGER.error("No writer found with the provided ID.");
            return;
        }
        LOGGER.info("Writer with id={} has been deleted successfully", writerService.deleteWriter(writerToDelete));
    }

    void viewAllWriters() {
        LOGGER.info("Viewing all writers");
        List<Writer> writers = writerService.viewAllWriters();
        writerView.displayWriters(writers);
    }
}
