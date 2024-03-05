package pl.malcew.publicmentoringmalcew.service;

import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Post;
import pl.malcew.publicmentoringmalcew.repo.LabelRepo;
import pl.malcew.publicmentoringmalcew.repo.PostRepo;

import java.util.List;

@Component
public class PostService {
    private final PostRepo postRepo;
    private final LabelRepo labelRepo;

    public PostService(PostRepo postRepo, LabelRepo labelRepo) {
        this.postRepo = postRepo;
        this.labelRepo = labelRepo;
    }

    public void createPost(Post post) {
        // Create the post and get the generated id
        Long postId =
                postRepo.create(post);

        // Create the labels and update the post_labels table
        post.labels().forEach(label -> {
            Long labelId = labelRepo.create(label);
            postRepo.addLabelToPost(postId, labelId);
        });
    }

    public Post readPost(Long id) {
       return postRepo.read(id);
    }

    public void updatePost(Post post) {
        postRepo.update(post);
    }

    public void deletePost(Post post) {
        postRepo.delete(post);
    }

    public List<Post> viewAllPosts() {
        return postRepo.viewAll();
    }
}
