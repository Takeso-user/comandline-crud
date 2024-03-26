package pl.malcew.publicmentoringmalcew.repo.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.malcew.publicmentoringmalcew.model.Post;
import pl.malcew.publicmentoringmalcew.model.PostStatus;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.model.WriterStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PostRepoImplTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private PostRepoImpl postRepoImpl;

    @BeforeEach
    public void setupTest() throws SQLException {
        MockitoAnnotations.openMocks(this);
        postRepoImpl = spy(new PostRepoImpl());
        doReturn(connection).when(postRepoImpl).getConnection();
    }

    @Test
    public void testIsWriterActiveTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(anyString())).thenReturn(1L);

        postRepoImpl.isWriterActive(1L);

        verify(preparedStatement, times(1)).setLong(1, 1L);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testCreatePostTest() throws SQLException {
        Post post = mock(Post.class);
        PostStatus postStatus = mock(PostStatus.class);
        Writer writer = mock(Writer.class);

        when(post.status()).thenReturn(postStatus);
        when(post.writer()).thenReturn(writer);
        when(postStatus.id()).thenReturn(1L);
        when(writer.id()).thenReturn(1L);

        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        postRepoImpl.createPost(post);

        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement, times(1)).getGeneratedKeys();
    }

    @Test
    public void testReadTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        when(resultSet.getString("content")).thenReturn("test");
        when(resultSet.getString("firstName")).thenReturn("test");
        when(resultSet.getString("lastName")).thenReturn("test");
        when(resultSet.getString("label_ids")).thenReturn(null);
        when(resultSet.getString("label_names")).thenReturn(null);
        when(resultSet.getString("label_status_ids")).thenReturn(null);
        when(resultSet.getObject(any(), eq(LocalDateTime.class))).thenReturn(LocalDateTime.now());
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getLong("status_id")).thenReturn(1L);

        postRepoImpl.read(1L);

        verify(preparedStatement, times(1)).setLong(1, 1L);
        verify(preparedStatement, times(1)).executeQuery();
    }


    @Test
    public void testViewAllTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        when(resultSet.getString("content")).thenReturn("test");
        when(resultSet.getString("firstName")).thenReturn("test");
        when(resultSet.getString("lastName")).thenReturn("test");
        when(resultSet.getString("label_ids")).thenReturn(null);
        when(resultSet.getString("label_names")).thenReturn(null);
        when(resultSet.getString("label_status_ids")).thenReturn(null);
        when(resultSet.getObject(any(), eq(LocalDateTime.class))).thenReturn(LocalDateTime.now());
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getLong("status_id")).thenReturn(1L);

        postRepoImpl.viewAll();

        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testUpdateTest() throws SQLException {
        Post post = new Post(1L, "content", LocalDateTime.now(), LocalDateTime.now(), List.of(), PostStatus.ACTIVE, new Writer(1L, "firstName", "lastName", null, WriterStatus.ACTIVE));
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        postRepoImpl.update(post);

        verify(preparedStatement, times(1)).setString(1, post.content());
        verify(preparedStatement, times(1)).setObject(2, post.created());
        verify(preparedStatement, times(1)).setObject(3, post.updated());
        verify(preparedStatement, times(1)).setString(4, post.status().name());
        verify(preparedStatement, times(1)).setLong(5, post.writer().id());
        verify(preparedStatement, times(1)).setLong(6, post.id());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteTest() throws SQLException {
        Post post = new Post(1L, "content", LocalDateTime.now(), LocalDateTime.now(), List.of(), PostStatus.ACTIVE, new Writer(1L, "firstName", "lastName", null, WriterStatus.ACTIVE));
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        postRepoImpl.delete(post);

        verify(preparedStatement, times(1)).setLong(1, post.id());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testAddLabelToPostTest() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        postRepoImpl.addLabelToPost(1L, 1L);

        verify(preparedStatement, times(1)).setLong(1, 1L);
        verify(preparedStatement, times(1)).setLong(2, 1L);
        verify(preparedStatement, times(1)).executeUpdate();
    }
}