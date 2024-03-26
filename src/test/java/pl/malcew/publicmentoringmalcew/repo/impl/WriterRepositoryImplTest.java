package pl.malcew.publicmentoringmalcew.repo.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.model.WriterStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class WriterRepositoryImplTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private WriterRepositoryImpl writerRepositoryImpl;

    @BeforeEach
    void setUpTest() {
        MockitoAnnotations.openMocks(this);
        writerRepositoryImpl = new WriterRepositoryImpl() {
            @Override
            protected Connection getConnection() {
                return connection;
            }
        };
    }

    @Test
    void createWriterSuccessfullyreturnsGeneratedIdTest() throws Exception {
        Writer writer = mock(Writer.class);
        WriterStatus writerStatus = mock(WriterStatus.class);

        when(writer.firstName()).thenReturn("John");
        when(writer.lastName()).thenReturn("Doe");
        when(writerStatus.getIdByName()).thenReturn(1L);

        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        Long result = writerRepositoryImpl.create(writer);

        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement, times(1)).getGeneratedKeys();
    }

    @Test
    void createWriterThrowsExceptionwhenNoIdObtainedTest() throws Exception {
        Writer writer = new Writer(1L, "John", "Doe", null, WriterStatus.ACTIVE);
        when(connection.prepareStatement(anyString(), (int) anyLong())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> writerRepositoryImpl.create(writer));
    }

    @Test
    void readWriterSuccessfullyreturnsWriterTest() throws Exception {
        Writer writer = new Writer(1L, "John", "Doe", null, WriterStatus.ACTIVE);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(writer.id());
        when(resultSet.getString("firstName")).thenReturn(writer.firstName());
        when(resultSet.getString("lastName")).thenReturn(writer.lastName());
        when(resultSet.getLong("status_id")).thenReturn(writer.status().getIdByName());

        Writer result = writerRepositoryImpl.read(1L);

        assertEquals(writer, result);
    }

    @Test
    void readWriterReturnsNullwhenNoWriterFoundTest() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Writer result = writerRepositoryImpl.read(1L);

        assertNull(result);
    }

    @Test
    void updateWriterSuccessfullyreturnsUpdatedWriterTest() throws Exception {
        Writer writer = new Writer(1L, "John", "Doe", null, WriterStatus.ACTIVE);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Writer result = writerRepositoryImpl.update(writer);

        assertEquals(writer, result);
    }

    @Test
    void deleteWriterSuccessfullyreturnsDeletedWriterIdTest() throws Exception {
        Writer writer = new Writer(1L, "John", "Doe", null, WriterStatus.ACTIVE);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Long result = writerRepositoryImpl.delete(writer);

        assertEquals(writer.id(), result);
    }

    @Test
    void deleteWriterReturnsZerowhenErrorOccursTest() throws Exception {
        Writer writer = new Writer(1L, "John", "Doe", null, WriterStatus.ACTIVE);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

        Long result = writerRepositoryImpl.delete(writer);

        assertEquals(0L, result);
    }
}