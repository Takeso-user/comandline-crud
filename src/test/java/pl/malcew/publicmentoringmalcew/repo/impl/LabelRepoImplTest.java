package pl.malcew.publicmentoringmalcew.repo.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.malcew.publicmentoringmalcew.model.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class LabelRepoImplTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private LabelRepoImpl labelRepoImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        labelRepoImpl = new LabelRepoImpl() {
            @Override
            protected Connection getConnection() {
                return connection;
            }
        };
    }

    @Test
    void createLabelSuccessfully_returnsGeneratedId() throws Exception {
        Label label = new Label(1L, "Test Label");
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        Long result = labelRepoImpl.create(label);

        assertEquals(1L, result);
    }

    @Test
    void createLabelThrowsException_whenNoIdObtained() throws Exception {
        Label label = new Label(1L, "Test Label");
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> labelRepoImpl.create(label));
    }

    @Test
    void readLabelSuccessfully_returnsLabel() throws Exception {
        Label label = new Label(1L, "Test Label");
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(label.id());
        when(resultSet.getString("name")).thenReturn(label.name());

        Label result = labelRepoImpl.read(1L);

        assertEquals(label, result);
    }

    @Test
    void readLabelReturnsNull_whenNoLabelFound() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Label result = labelRepoImpl.read(1L);

        assertNull(result);
    }

    @Test
    void viewAllLabelsSuccessfully_returnsAllLabels() throws Exception {
        List<Label> labels = List.of(
                new Label(1L, "Test Label 1"),
                new Label(2L, "Test Label 2"));

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong(1)).thenReturn(1L, 2L); // Assuming "id" is the first column in your SQL query
        when(resultSet.getString(2)).thenReturn("Test Label 1", "Test Label 2"); // Assuming "name" is the second column in your SQL query

        List<Label> result = labelRepoImpl.viewAll();

        // Loop through each label in the result list
        for (int i = 0; i < result.size(); i++) {
            // Compare each field of the Label record one by one
            assertEquals(labels.get(i).id(), result.get(i).id());
            assertEquals(labels.get(i).name(), result.get(i).name());
        }
    }

    @Test
    void updateLabelSuccessfully_returnsUpdatedLabel() throws Exception {
        Label label = new Label(1L, "Test Label");
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Label result = labelRepoImpl.update(label);

        assertEquals(label, result);
    }

    @Test
    void deleteLabelSuccessfully_returnsDeletedLabelId() throws Exception {
        Label label = new Label(1L, "Test Label");
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Long result = labelRepoImpl.delete(label);

        assertEquals(label.id(), result);
    }

    @Test
    void deleteLabelReturnsZero_whenErrorOccurs() throws Exception {
        Label label = new Label(1L, "Test Label");
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

        Long result = labelRepoImpl.delete(label);

        assertEquals(0L, result);
    }
}