package pl.malcew.publicmentoringmalcew.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.malcew.publicmentoringmalcew.model.Post;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.model.WriterStatus;
import pl.malcew.publicmentoringmalcew.service.PostService;
import pl.malcew.publicmentoringmalcew.service.WriterService;
import pl.malcew.publicmentoringmalcew.view.PostView;
import pl.malcew.publicmentoringmalcew.view.WriterView;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class PostControllerTest {

    @Mock
    private PostView postView;

    @Mock
    private WriterView writerView;

    @Mock
    private PostService postService;

    @Mock
    private WriterService writerService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPostSuccessfully() {
        Writer writer = new Writer(
                null,
                "Test ",
                "Writer",
                null,
                WriterStatus.ACTIVE
        );
        Post post = new Post(
                1L,
                "Test Post",
                null,
                null,
                null,
                null,
                null
        );
        Post createdPost = new Post(
                1L,
                "Test Post",
                null,
                null,
                null,
                null,
                writer
        );
        when(writerView.provideWriterId()).thenReturn(1L);
        when(writerService.readWriter(anyLong())).thenReturn(writer);
        when(postView.createPost(any(Writer.class))).thenReturn(post);
        when(postService.createPost(any(Post.class))).thenReturn(1L);

        postController.createPost();

        verify(writerView, times(1)).provideWriterId();
        verify(writerService, times(1)).readWriter(anyLong());
        verify(postView, times(1)).createPost(any(Writer.class));
        verify(postService, times(1)).createPost(any(Post.class));
    }

    @Test
    void readPostSuccessfully() {
        when(postView.providePostId()).thenReturn(1L);
        when(postService.readPost(1L)).thenReturn(new Post(
                1L,
                "Test Post",
                null,
                null,
                null,
                null,
                null
        ));

        postController.readPost();

        verify(postView, times(1)).providePostId();
        verify(postService, times(1)).readPost(1L);
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
        when(postView.providePostId()).thenReturn(1L);
        when(postService.readPost(anyLong())).thenReturn(post);
        when(postView.updatePost(any(Post.class))).thenReturn(post);
        when(postService.updatePost(any(Post.class))).thenReturn(post);

        postController.updatePost();

        verify(postView, times(1)).providePostId();
        verify(postService, times(1)).readPost(anyLong());
        verify(postView, times(1)).updatePost(any(Post.class));
        verify(postService, times(1)).updatePost(any(Post.class));
    }

    @Test
    void deletePostSuccessfully() {
        when(postView.providePostId()).thenReturn(1L);
        when(postService.deletePost(anyLong())).thenReturn(1L);

        postController.deletePost();

        verify(postView, times(1)).providePostId();
        verify(postService, times(1)).deletePost(anyLong());
    }

    @Test
    void viewAllPostsSuccessfully() {
        List<Post> posts = Arrays.asList(
                new Post(
                        1L,
                        "Test Post 1",
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                new Post(
                        2L,
                        "Test Post 2",
                        null,
                        null,
                        null,
                        null,
                        null
                ));
        when(postService.viewAllPosts()).thenReturn(posts);

        postController.viewAllPosts();

        verify(postService, times(1)).viewAllPosts();
    }
}