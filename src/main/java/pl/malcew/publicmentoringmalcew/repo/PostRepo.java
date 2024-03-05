package pl.malcew.publicmentoringmalcew.repo;

import pl.malcew.publicmentoringmalcew.model.Post;

public interface PostRepo extends GenericRepository<Post, Long>{
    void addLabelToPost(Long postId, Long labelId);
}
