package pl.malcew.publicmentoringmalcew.model;

public record Label(Long id, String name, LabelStatus status) {
    public Label{
        if (status == null) {
            status = LabelStatus.ACTIVE;
        }
    }
}
