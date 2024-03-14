package pl.malcew.publicmentoringmalcew.model;

import java.util.List;

public record Writer (
    Long id,
    String firstName,
    String lastName,
    List<Post> posts
){
    @Override
    public String toString() {
        return "Writer[" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ']';
    }
}