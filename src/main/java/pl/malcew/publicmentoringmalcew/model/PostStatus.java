package pl.malcew.publicmentoringmalcew.model;

public enum PostStatus {
    ACTIVE(1),
    UNDER_REVIEW(2),
    DELETED(3),
    DRAFT(4);
    private final Long id;


    PostStatus(int id) {
        this.id = Long.parseLong(String.valueOf(id));
    }

    public Long id() {
        return id;
    }
}
