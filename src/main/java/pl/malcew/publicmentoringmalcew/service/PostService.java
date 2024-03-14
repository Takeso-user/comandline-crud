package pl.malcew.publicmentoringmalcew.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Post;
import pl.malcew.publicmentoringmalcew.repo.LabelRepo;
import pl.malcew.publicmentoringmalcew.repo.PostRepo;

import java.util.List;

@Component
public class PostService {
    private final PostRepo postRepo;
    private final LabelRepo labelRepo;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    public PostService(PostRepo postRepo, LabelRepo labelRepo) {
        this.postRepo = postRepo;
        this.labelRepo = labelRepo;
    }

    public Long createPost(Post post) {
        LOGGER.info("Creating post: {}", post);
        Long postId = postRepo.create(post);
        post.labels().forEach(label -> {
            Long labelId = labelRepo.create(label);
            postRepo.addLabelToPost(postId, labelId);
        });
        LOGGER.info("Post created with id: {}", postId);
        return postId;
    }

    public Post readPost(Long id) {
        LOGGER.info("Reading post with id: {}", id);
        Post post = postRepo.read(id);
        LOGGER.info("Post read: {}", post);
        return post;
    }

    public Post updatePost(Post post) {
        LOGGER.info("Updating post: {}", post);
        Post updatedPost = postRepo.update(post);
        LOGGER.info("Post updated: {}", updatedPost);
        return updatedPost;
    }

    public Long deletePost(Long id) {
        LOGGER.info("Deleting post with id: {}", id);
        var post = postRepo.read(id);
        try {
            Long deletedId = postRepo.delete(post);
            LOGGER.info("Post deleted with id: {}", deletedId);
            return deletedId;
        } catch (Exception e) {
            LOGGER.error("Error deleting post: ", e);
        }
        return null;
    }

    public List<Post> viewAllPosts() {
        LOGGER.info("Viewing all posts");
        List<Post> posts = postRepo.viewAll();
        LOGGER.info("Posts viewed: {}", posts);
        return posts;
    }
}