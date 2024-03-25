package pl.malcew.publicmentoringmalcew.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.model.WriterStatus;
import pl.malcew.publicmentoringmalcew.service.WriterService;
import pl.malcew.publicmentoringmalcew.view.WriterView;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class WriteControllerTest {

    @Mock
    private WriterView writerView;

    @Mock
    private WriterService writerService;

    @InjectMocks
    private WriteController writeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWriterSuccessfully() {
        Writer writer = new Writer(null, "Test", "Writer", null, WriterStatus.ACTIVE);
        when(writerView.readWriter()).thenReturn(writer);
        when(writerService.createWriter(any(Writer.class))).thenReturn(1L);

        writeController.createWriter();

        verify(writerView, times(1)).readWriter();
        verify(writerService, times(1)).createWriter(any(Writer.class));
    }

    @Test
    void updateWriterSuccessfully() {
        Writer existingWriter = new Writer(null, "Test", "Writer", null, WriterStatus.ACTIVE);
        when(writerView.provideWriterId()).thenReturn(1L);
        when(writerService.readWriter(anyLong())).thenReturn(existingWriter);
        when(writerView.updateWriter(any(Writer.class))).thenReturn(existingWriter);
        when(writerService.updateWriter(any(Writer.class))).thenReturn(existingWriter);

        writeController.updateWriter();

        verify(writerView, times(1)).provideWriterId();
        verify(writerService, times(1)).readWriter(anyLong());
        verify(writerView, times(1)).updateWriter(any(Writer.class));
        verify(writerService, times(1)).updateWriter(any(Writer.class));
    }

    @Test
    void deleteWriterSuccessfully() {
        Writer writerToDelete = new Writer(null, "Test", "Writer", null, WriterStatus.ACTIVE);
        when(writerView.provideWriterId()).thenReturn(1L);
        when(writerService.readWriter(anyLong())).thenReturn(writerToDelete);
        when(writerService.deleteWriter(any(Writer.class))).thenReturn(1L);

        writeController.deleteWriter();

        verify(writerView, times(1)).provideWriterId();
        verify(writerService, times(1)).readWriter(anyLong());
        verify(writerService, times(1)).deleteWriter(any(Writer.class));
    }

    @Test
    void viewAllWritersSuccessfully() {
        List<Writer> writers = Arrays.asList(
                new Writer(null, "Test1", "Writer1", null, WriterStatus.ACTIVE),
                new Writer(null, "Test2", "Writer2", null, WriterStatus.ACTIVE));
        when(writerService.viewAllWriters()).thenReturn(writers);

        writeController.viewAllWriters();

        verify(writerService, times(1)).viewAllWriters();
    }
}