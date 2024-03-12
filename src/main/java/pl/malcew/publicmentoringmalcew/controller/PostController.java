package pl.malcew.publicmentoringmalcew.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Post;
import pl.malcew.publicmentoringmalcew.model.Writer;
import pl.malcew.publicmentoringmalcew.service.PostService;
import pl.malcew.publicmentoringmalcew.service.WriterService;
import pl.malcew.publicmentoringmalcew.view.PostView;
import pl.malcew.publicmentoringmalcew.view.WriterView;

import java.util.List;

@Component
public class PostController {
    private final PostView postView;
    private final PostService postService;
    private final WriterService writerService;
    private final WriterView writerView;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    public PostController(PostView postView, PostService postService, WriterService writerService, WriterView writerView) {
        this.postView = postView;
        this.postService = postService;
        this.writerService = writerService;
        this.writerView = writerView;
    }

    public void handleMenu() {
        postView.displayMenu();
        int choice = postView.readOption();
        switch (choice) {
            case 1:
                createPost();
                break;
            case 2:
                readPost();
                break;
            case 3:
                updatePost();
                break;
            case 4:
                deletePost();
                break;
            case 5:
                viewAllPosts();
                break;
            case 0:
                return;
            default:
                System.err.println(("Invalid choice"));
        }
    }

    private void viewAllPosts() {
        List<Post> posts = postService.viewAllPosts();
        LOGGER.info("Viewing all posts");
        postView.displayPosts(posts);
    }

    private void deletePost() {
        Long id = postView.providePostId();
        LOGGER.info("Post with id={} is deleted", postService.deletePost(id));

    }

    private void updatePost() {
        Post post = postService.readPost(postView.providePostId());
        if(post == null) {
            LOGGER.error("Post not found");
            return;
        }
        Post updatedPost = postView.updatePost(post);
        LOGGER.info("Post {} has been updated", postService.updatePost(updatedPost));

    }

    private void readPost() {
        Long id = postView.providePostId();
        LOGGER.info("Reading post {}", postService.readPost(id));
    }


    private void createPost() {
        Writer writer = getWriter();
        Post post = postView.createPost(writer);
        LOGGER.info("Creating new post {}", postService.createPost(post));
    }

    private Writer getWriter() {
        writerView.displayWriters(writerService.viewAllWriters());
        Long writerId = writerView.provideWriterId();
        var writer = writerService.readWriter(writerId);
        LOGGER.info("Writer {} has been selected", writer);
        return writer;
    }
}
