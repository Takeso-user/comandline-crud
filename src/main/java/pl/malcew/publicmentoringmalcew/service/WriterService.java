package pl.malcew.publicmentoringmalcew.service.WriterService;

import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.repo.WriterRepo;

import java.util.List;

public class WriterService {
    private final WriterRepo writerRepo;

    public WriterService(WriterRepo writerRepo) {
        this.writerRepo = writerRepo;
    }

    public void createWriter(Writer writer) {
        writerRepo.create(writer);
    }

    public Writer readWriter(Long id) {
        return writerRepo.read(id);
    }

    public void updateWriter(Writer writer) {
        writerRepo.update(writer);
    }

    public void deleteWriter(Writer writer) {
        writerRepo.delete(writer);
    }

    public List<Writer> viewAllWriters() {
        return writerRepo.viewAll();
    }


}
