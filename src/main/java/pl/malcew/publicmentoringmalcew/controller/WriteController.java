package pl.malcew.publicmentoringmalcew.controller;

import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.service.WriterService;
import pl.malcew.publicmentoringmalcew.view.WriterView;

import java.util.List;

@Component
public class WriteController {

    private final WriterView writerView;
    private final WriterService writerService;


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
        return writerView.provideWriterId();
    }

    private void createWriter() {
        String firstName = writerView.readWriter().firstName();
        String lastName = writerView.readWriter().lastName();
        Writer wr = new Writer(firstName, lastName);
        writerService.createWriter(wr);
        System.out.println("Writer created successfully.\n" + "Writer: " + wr);
    }

    private void updateWriter() {
        Writer existingWriter = writerService.readWriter(getWriterID());
        if (existingWriter == null) {
            System.out.println("No writer found with the provided ID.");
            return;
        }
        Writer updatedWriter = writerView.updateWriter(existingWriter);
        writerService.updateWriter(updatedWriter);
        System.out.println("Writer updated successfully.");
    }

    private void deleteWriter() {
        Writer writerToDelete = writerService.readWriter(getWriterID());
        if (writerToDelete == null) {
            System.out.println("No writer found with the provided ID.");
            return;
        }
        writerService.deleteWriter(writerToDelete);
        System.out.println("Writer deleted successfully.");
        // Similar to createWriter
    }

    private void viewAllWriters() {
        List<Writer> writers = writerService.viewAllWriters();
        writerView.displayWriters(writers);
        // Call writerService to get all writers
        // Call writerView to display all writers
    }
}
