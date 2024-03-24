package pl.malcew.publicmentoringmalcew.model;

import java.util.Arrays;

public enum WriterStatus {
    ACTIVE(1L),
    DELETED(2L);
    private final Long id;

    WriterStatus(Long id) {
        this.id = id;
    }
    public static WriterStatus getById(Long id) {
        return Arrays.stream(WriterStatus.values())
                .filter(status -> status.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid WriterStatus id: " + id));
    }
    public Long getIdByName() {
        return id;
    }

}
