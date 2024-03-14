package pl.malcew.publicmentoringmalcew.repo.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.model.Post;
import pl.malcew.publicmentoringmalcew.model.PostStatus;
import pl.malcew.publicmentoringmalcew.model.Writer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PostRepoImplTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private PostRepoImpl postRepoImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postRepoImpl = new PostRepoImpl() {
            @Override
            protected Connection getConnection() {
                return connection;
            }
        };


    }

    @Test
    void createPostSuccessfully_returnsGeneratedId() throws Exception {
        Writer writer = new Writer(4L, "John", "Doe", null);

        Post post = new Post(
                1L,
                "Test Post",
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(new Label(1L, "Fake")),
                PostStatus.ACTIVE,
                writer);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        Long result = postRepoImpl.create(post);

        assertEquals(1L, result);
    }

    @Test
    void readPostSuccessfully_returnsPost() throws Exception {
        Writer writer = new Writer(4L, "John", "Doe", null);

        Post post = new Post(
                1L,
                "Test Post",
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(new Label(1L, "Fake1"), new Label(2L, "Fake2")),
                PostStatus.ACTIVE,
                writer);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(post.id());
        when(resultSet.getString("content")).thenReturn(post.content());
        when(resultSet.getObject("created", LocalDateTime.class)).thenReturn(post.created());
        when(resultSet.getObject("updated", LocalDateTime.class)).thenReturn(post.updated());
        when(resultSet.getObject("status", PostStatus.class)).thenReturn(post.status());
        when(resultSet.getString("firstName")).thenReturn(writer.firstName());
        when(resultSet.getString("lastName")).thenReturn(writer.lastName());

        when(resultSet.getString("label_ids")).thenReturn("1,2");
        when(resultSet.getString("label_names")).thenReturn("Fake1,Fake2");


        Post result = postRepoImpl.read(1L);

        assertEquals(post.id(), result.id());
        assertEquals(post.content(), result.content());
        assertEquals(post.created(), result.created());
        assertEquals(post.updated(), result.updated());
        assertEquals(post.status(), result.status());
        assertEquals(post.writer().firstName(), result.writer().firstName());
        assertEquals(post.writer().lastName(), result.writer().lastName());

        assertIterableEquals(post.labels(), result.labels());
    }

    @Test
    void viewAllPostsSuccessfully_returnsAllPosts() throws Exception {

        Writer writer = new Writer(4L, "jj", "zue", null);

        // Создаем объект Post
        Post post = new Post(
                2L,
                "Test Post",
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(new Label(1L, "Fake1"), new Label(2L, "Fake2")),
                PostStatus.ACTIVE,
                writer);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(post.id());
        when(resultSet.getString("content")).thenReturn(post.content());
        when(resultSet.getObject("created", LocalDateTime.class)).thenReturn(post.created());
        when(resultSet.getObject("updated", LocalDateTime.class)).thenReturn(post.updated());
        when(resultSet.getObject("status", PostStatus.class)).thenReturn(post.status());
        when(resultSet.getString("firstName")).thenReturn(writer.firstName());
        when(resultSet.getString("lastName")).thenReturn(writer.lastName());

        when(resultSet.getString("label_ids")).thenReturn("1,2");
        when(resultSet.getString("label_names")).thenReturn("Fake1,Fake2");


        List<Post> result = postRepoImpl.viewAll();

        assertEquals(1, result.size());
        Post actualPost = result.get(0);
        assertEquals(post.id(), actualPost.id());
        assertEquals(post.content(), actualPost.content());
        assertEquals(post.created(), actualPost.created());
        assertEquals(post.updated(), actualPost.updated());
        assertEquals(post.status(), actualPost.status());
        assertEquals(post.writer().firstName(), actualPost.writer().firstName());
        assertEquals(post.writer().lastName(), actualPost.writer().lastName());

        assertIterableEquals(post.labels(), actualPost.labels());

    }

    @Test
    void deletePostSuccessfully_returnsDeletedPostId() throws Exception {
        Writer writer = new Writer(4L, "John", "Doe", null);

        Post post = new Post(
                1L,
                "Test Post",
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(new Label(1L, "Fake")),
                PostStatus.ACTIVE,
                writer);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Long result = postRepoImpl.delete(post);

        assertEquals(post.id(), result);
    }

    @Test
    void addLabelToPostSuccessfully_addsLabel() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        assertDoesNotThrow(() -> postRepoImpl.addLabelToPost(1L, 1L));
    }
}