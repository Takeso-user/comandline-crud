package pl.malcew.publicmentoringmalcew.service;

import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Post;
import pl.malcew.publicmentoringmalcew.repo.PostRepo;

import java.util.List;

@Component
public class PostService {
    private final PostRepo postRepo;

    public PostService(PostRepo postRepo) {
        this.postRepo = postRepo;
    }

    public void createPost(Post post) {
        postRepo.create(post);
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
