package pl.malcew.publicmentoringmalcew.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.repo.LabelRepo;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LabelServiceTest {

    @Mock
    private LabelRepo labelRepo;

    @InjectMocks
    private LabelService labelService;

    @BeforeEach
    void setUpTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLabelSuccessfullyTest() {
        when(labelRepo.create(any(Label.class))).thenReturn(1L);

        Long id = labelService.createLabel("Test Label");

        assertEquals(1L, id);
        verify(labelRepo, times(1)).create(any(Label.class));
    }

    @Test
    void readLabelSuccessfullyTest() {
        Label label = new Label(1L, "Test Label", null);
        when(labelRepo.read(1L)).thenReturn(label);

        Label result = labelService.readLabel(1L);

        assertEquals(label, result);
        verify(labelRepo, times(1)).read(1L);
    }

    @Test
    void updateLabelSuccessfullyTest() {
        Label label = new Label(1L, "Test Label",null);
        when(labelRepo.update(label)).thenReturn(label);

        Label result = labelService.updateLabel(label);

        assertEquals(label, result);
        verify(labelRepo, times(1)).update(label);
    }

    @Test
    void deleteLabelSuccessfullyTest() {
        Label label = new Label(1L, "Test Label", null);
        when(labelRepo.read(1L)).thenReturn(label);
        when(labelRepo.delete(label)).thenReturn(1L);

        Long id = labelService.deleteLabel(1L);

        assertEquals(1L, id);
        verify(labelRepo, times(1)).delete(label);
    }

    @Test
    void deleteLabelUnsuccessfullyTest() {
        Label label = new Label(1L, "Test Label", null);
        when(labelRepo.read(1L)).thenReturn(label);
        when(labelRepo.delete(label)).thenThrow(new RuntimeException());

        Long id = labelService.deleteLabel(1L);

        assertEquals(id, 0L);
        verify(labelRepo, times(1)).delete(label);
    }

    @Test
    void viewAllLabelsSuccessfullyTest() {
        List<Label> labels = Arrays.asList(
                new Label(1L, "Test Label 1",null),
                new Label(2L, "Test Label 2",null));
        when(labelRepo.viewAll()).thenReturn(labels);

        List<Label> result = labelService.viewAllLabels();

        assertEquals(labels, result);
        verify(labelRepo, times(1)).viewAll();
    }
}