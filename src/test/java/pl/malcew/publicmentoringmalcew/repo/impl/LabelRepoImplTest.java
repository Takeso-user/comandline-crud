package pl.malcew.publicmentoringmalcew.repo.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.model.LabelStatus;

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
    void createLabelSuccessfullyreturnsGeneratedIdTest() throws Exception {
        Label label = new Label(1L, "Test Label", null);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        Long result = labelRepoImpl.create(label);

        assertEquals(1L, result);
    }

    @Test
    void createLabelThrowsExceptionwhenNoIdObtainedTest() throws Exception {
        Label label = new Label(1L, "Test Label", null);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> labelRepoImpl.create(label));
    }

    @Test
    void readLabelSuccessfullyreturnsLabelTest() throws Exception {
        Label label = new Label(1L, "Test Label", LabelStatus.ACTIVE);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(label.id());
        when(resultSet.getString("name")).thenReturn(label.name());
        when(resultSet.getLong("status_id")).thenReturn(label.status().getIdByName());

        Label result = labelRepoImpl.read(1L);

        assertEquals(label, result);
    }

    @Test
    void readLabelReturnsNullwhenNoLabelFoundTest() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Label result = labelRepoImpl.read(1L);

        assertNull(result);
    }

    @Test
    void viewAllLabelsSuccessfullyreturnsAllLabelsTest() throws Exception {
        List<Label> labels = List.of(
                new Label(1L, "Test Label 1", null),
                new Label(2L, "Test Label 2", null));

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getLong(1)).thenReturn(1L, 2L); // Assuming "id" is the first column in your SQL query
        when(resultSet.getString(2)).thenReturn("Test Label 1", "Test Label 2"); // Assuming "name" is the second column in your SQL query

        List<Label> result = labelRepoImpl.viewAll();


        for (int i = 0; i < result.size(); i++) {
            assertEquals(labels.get(i).id(), result.get(i).id());
            assertEquals(labels.get(i).name(), result.get(i).name());
        }
    }

    @Test
    void updateLabelSuccessfullyreturnsUpdatedLabelTest() throws Exception {
        Label label = new Label(1L, "Test Label", null);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Label result = labelRepoImpl.update(label);

        assertEquals(label, result);
    }

    @Test
    void deleteLabelSuccessfullyreturnsDeletedLabelIdTest() throws Exception {
        Label label = new Label(1L, "Test Label", null);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Long result = labelRepoImpl.delete(label);

        assertEquals(label.id(), result);
    }

    @Test
    void deleteLabelReturnsZerowhenErrorOccursTest() throws Exception {
        Label label = new Label(1L, "Test Label", null);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

        Long result = labelRepoImpl.delete(label);

        assertEquals(0L, result);
    }
}