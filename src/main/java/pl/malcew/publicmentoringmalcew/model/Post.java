package pl.malcew.publicmentoringmalcew.model;

import java.time.LocalDateTime;
import java.util.List;

public record Post (
    Long id,
    String content,
    LocalDateTime created,
    LocalDateTime updated,
    List<Label> lables
){}
