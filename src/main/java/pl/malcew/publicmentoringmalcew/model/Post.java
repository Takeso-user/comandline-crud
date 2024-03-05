package pl.malcew.publicmentoringmalcew.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record Post (
    Long id,
    String content,
    LocalDateTime created,
    LocalDateTime updated,
    List<Label> labels,
    PostStatus status,
    Writer writer
) {
    public Post {
        if (labels == null) {
            labels = new ArrayList<>();
        }
    }
}
