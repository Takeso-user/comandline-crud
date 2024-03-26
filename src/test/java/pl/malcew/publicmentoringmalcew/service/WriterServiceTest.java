package pl.malcew.publicmentoringmalcew.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.model.WriterStatus;
import pl.malcew.publicmentoringmalcew.repo.WriterRepo;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WriterServiceTest {

    @Mock
    private WriterRepo writerRepo;

    @InjectMocks
    private WriterService writerService;

    @BeforeEach
    void setUpTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWriterSuccessfullyTest() {
        Writer writer = new Writer(
                null,
                "Test ",
                "Writer",
                null,
                WriterStatus.ACTIVE
        );
        when(writerRepo.create(any(Writer.class))).thenReturn(1L);

        Long id = writerService.createWriter(writer);

        assertEquals(1L, id);
        verify(writerRepo, times(1)).create(any(Writer.class));
    }

    @Test
    void readWriterSuccessfullyTest() {
        Writer writer = new Writer(
                null,
                "Test",
                " Writer",
                null,
                WriterStatus.ACTIVE
        );
        when(writerRepo.read(1L)).thenReturn(writer);

        Writer result = writerService.readWriter(1L);

        assertEquals(writer, result);
        verify(writerRepo, times(1)).read(1L);
    }

    @Test
    void updateWriterSuccessfullyTest() {
        Writer writer = new Writer(
                null,
                "Test",
                " Writer",
                null,
                WriterStatus.ACTIVE
        );
        when(writerRepo.update(writer)).thenReturn(writer);

        Writer result = writerService.updateWriter(writer);

        assertEquals(writer, result);
        verify(writerRepo, times(1)).update(writer);
    }

    @Test
    void deleteWriterSuccessfullyTest() {
        Writer writer = new Writer(
                null,
                "Test",
                " Writer",
                null,
                WriterStatus.ACTIVE
        );
        when(writerRepo.read(1L)).thenReturn(writer);
        when(writerRepo.delete(writer)).thenReturn(1L);

        Long id = writerService.deleteWriter(writer);

        assertEquals(1L, id);
        verify(writerRepo, times(1)).delete(writer);
    }

    @Test
    void deleteWriterUnsuccessfullyTest() {
        Writer writer = new Writer(
                null,
                "Test",
                " Writer",
                null,
                WriterStatus.ACTIVE
        );
        when(writerRepo.read(1L)).thenReturn(writer);
        when(writerRepo.delete(writer)).thenThrow(new RuntimeException());

        Long id = writerService.deleteWriter(writer);

        assertEquals(0L, id);
        verify(writerRepo, times(1)).delete(writer);
    }

    @Test
    void viewAllWritersSuccessfullyTest() {
        List<Writer> writers = Arrays.asList(
                new Writer(
                        null,
                        "Test",
                        " Writer",
                        null,
                        WriterStatus.ACTIVE
                ),
                new Writer(
                        null,
                        "Test2",
                        " Writer2",
                        null,
                        WriterStatus.ACTIVE
                ));
        when(writerRepo.viewAll()).thenReturn(writers);

        List<Writer> result = writerService.viewAllWriters();

        assertEquals(writers, result);
        verify(writerRepo, times(1)).viewAll();
    }
}