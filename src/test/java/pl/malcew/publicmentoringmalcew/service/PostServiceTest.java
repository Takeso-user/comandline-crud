package pl.malcew.publicmentoringmalcew.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.malcew.publicmentoringmalcew.model.Post;
import pl.malcew.publicmentoringmalcew.repo.LabelRepo;
import pl.malcew.publicmentoringmalcew.repo.PostRepo;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepo postRepo;

    @Mock
    private LabelRepo labelRepo;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPostSuccessfully() {
        Post post = new Post(
                1L,
                "Test Post",
                null,
                null,
                null,
                null,
                null
        );
        when(postRepo.create(any(Post.class))).thenReturn(1L);

        Long result = postService.createPost(post);

        assertEquals(post.id(), result);
        verify(postRepo, times(1)).create(any(Post.class));
    }

    @Test
    void readPostSuccessfully() {
        Post post = new Post(
                1L,
                "Test Post",
                null,
                null,
                null,
                null,
                null
        );
        when(postRepo.read(1L)).thenReturn(post);

        Post result = postService.readPost(1L);

        assertEquals(post, result);
        verify(postRepo, times(1)).read(1L);
    }

    @Test
    void updatePostSuccessfully() {
        Post post = new Post(
                1L,
                "Test Post",
                null,
                null,
                null,
                null,
                null
        );
        when(postRepo.update(post)).thenReturn(post);

        Post result = postService.updatePost(post);

        assertEquals(post, result);
        verify(postRepo, times(1)).update(post);
    }

    @Test
    void deletePostSuccessfully() {
        Post post = new Post(
                1L,
                "Test Post",
                null,
                null,
                null,
                null,
                null
        );
        when(postRepo.read(1L)).thenReturn(post);
        when(postRepo.delete(post)).thenReturn(1L);

        Long id = postService.deletePost(1L);

        assertEquals(1L, id);
        verify(postRepo, times(1)).delete(post);
    }

    @Test
    void deletePostUnsuccessfully() {
        Post post = new Post(
                1L,
                "Test Post",
                null,
                null,
                null,
                null,
                null
        );
        when(postRepo.read(1L)).thenReturn(post);
        when(postRepo.delete(post)).thenThrow(new RuntimeException());

        Long id = postService.deletePost(1L);

        assertNull(id);
        verify(postRepo, times(1)).delete(post);
    }

    @Test
    void viewAllPostsSuccessfully() {
        List<Post> posts = Arrays.asList(
                new Post(
                        3L,
                        "Test Post 1",
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                new Post(
                        4L,
                        "Test Post 2",
                        null,
                        null,
                        null,
                        null,
                        null
                ));
        when(postRepo.viewAll()).thenReturn(posts);

        List<Post> result = postService.viewAllPosts();

        assertEquals(posts, result);
        verify(postRepo, times(1)).viewAll();
    }
}