package pl.malcew.publicmentoringmalcew.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.model.LabelStatus;
import pl.malcew.publicmentoringmalcew.service.LabelService;
import pl.malcew.publicmentoringmalcew.view.LabelView;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class LabelControllerTest {

    @Mock
    private LabelView labelView;

    @Mock
    private LabelService labelService;

    @InjectMocks
    private LabelController labelController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLabelSuccessfullyTest() {
        when(labelView.provideLabelName()).thenReturn("Test Label");
        when(labelService.createLabel(anyString())).thenReturn(1L);

        labelController.createLabel();

        verify(labelView, times(1)).provideLabelName();
        verify(labelService, times(1)).createLabel(anyString());
    }

    @Test
    void readLabelSuccessfullyTest() {
        Label label = new Label(1L, "Test Label", LabelStatus.ACTIVE);
        when(labelView.provideLabelId()).thenReturn(1L);
        when(labelService.readLabel(anyLong())).thenReturn(label);

        labelController.readLabel();

        verify(labelView, times(1)).provideLabelId();
        verify(labelService, times(1)).readLabel(anyLong());
    }

    @Test
    void updateLabelSuccessfullyTest() {
        Label label = new Label(1L, "Test Label", LabelStatus.ACTIVE);
        when(labelView.provideLabelId()).thenReturn(1L);
        when(labelView.provideLabelName()).thenReturn("Test Label");
        when(labelService.updateLabel(any(Label.class))).thenReturn(label);

        labelController.updateLabel();

        verify(labelView, times(1)).provideLabelId();
        verify(labelView, times(1)).provideLabelName();
        verify(labelService, times(1)).updateLabel(any(Label.class));
    }

    @Test
    void deleteLabelSuccessfullyTest() {
        when(labelView.provideLabelId()).thenReturn(1L);
        when(labelService.deleteLabel(anyLong())).thenReturn(1L);

        labelController.deleteLabel();

        verify(labelView, times(1)).provideLabelId();
        verify(labelService, times(1)).deleteLabel(anyLong());
    }

    @Test
    void viewAllLabelsSuccessfullyTest() {
        List<Label> labels = Arrays.asList(
                new Label(1L, "Test Label 1", LabelStatus.ACTIVE),
                new Label(2L, "Test Label 2", LabelStatus.ACTIVE));
        when(labelService.viewAllLabels()).thenReturn(labels);

        labelController.viewAllLabels();

        verify(labelService, times(1)).viewAllLabels();
    }
}