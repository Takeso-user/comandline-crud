package pl.malcew.publicmentoringmalcew.model;

import java.util.Arrays;

public enum LabelStatus {
    ACTIVE(1L),
    DELETED(2L);
    private final Long id;

    LabelStatus(Long id) {
        this.id = id;
    }

    public static LabelStatus getById(Long id) {
        return Arrays.stream(LabelStatus.values())
                .filter(status -> status.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid LabelStatus id: " + id));
    }

    public long getIdByName() {
        return id;
    }
}
