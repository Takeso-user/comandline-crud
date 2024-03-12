package pl.malcew.publicmentoringmalcew.view;

import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Label;
import pl.malcew.publicmentoringmalcew.model.Post;
import pl.malcew.publicmentoringmalcew.model.PostStatus;
import pl.malcew.publicmentoringmalcew.model.Writer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class PostView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("1. Create Post");
        System.out.println("2. Read Post");
        System.out.println("3. Update Post");
        System.out.println("4. Delete Post");
        System.out.println("5. View all Posts");
        System.out.println("6. Back to main menu");
    }

    public int readOption() {
        System.out.print("Enter option: ");
        return scanner.nextInt();
    }

    public void readPost() {
        System.out.println("Enter Post ID: ");
        Long id = scanner.nextLong();
    }

    public Post createPost(Writer writer) {
        LocalDateTime created = LocalDateTime.now();
        List<Label> labels = new ArrayList<>();
        System.out.println("Enter content: ");
        scanner.nextLine();
        String content = scanner.nextLine();

        while (true) {
            Label label = enterLabel();
            labels.add(label);
            System.out.println("Do you want to add another label? (y/n)");
            String answer = scanner.nextLine();
            if (answer.equals("n")) {
                break;
            } else if (!answer.equals("y")) {
                System.out.println("Invalid option. Please try again.");
            }
        }
        return new Post(null, content, created, created, labels, PostStatus.ACTIVE, writer);
    }


    public void displayPosts(List<Post> posts) {
        String leftAlignFormat = "| %-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |%n";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        System.out.format("+-----------------+-----------------+------------------+------------------+-----------------+-----------------+-----------------+%n");
        System.out.format("| ID              | Content         | Created          | Updated          | Label           | Status          | Writer          |%n");
        System.out.format("+-----------------+-----------------+------------------+------------------+-----------------+-----------------+-----------------+%n");
        for (Post post : posts) {
            List<String> labels = post.labels().stream().map(Label::name).toList();
            String content = post.content().length() > 15 ? post.content().substring(0, 15) : post.content();
            String writer =
                    (post.writer().firstName()+" "+post.writer().lastName()).length() > 15 ?
                            (post.writer().firstName()+" "+post.writer().lastName()).substring(0, 15) :
                            (post.writer().firstName()+" "+post.writer().lastName());
            String created = post.created().format(formatter);
            String updated = post.updated().format(formatter);
            if (labels.isEmpty()) {
                System.out.format(leftAlignFormat, post.id(), content, created, updated, "", post.status(), writer);
            } else {
                for (String label : labels) {
                    String trimmedLabel = label.length() > 15 ? label.substring(0, 15) : label;
                    System.out.format(leftAlignFormat, post.id(), content, created, updated, trimmedLabel, post.status(), writer);
                }
            }
        }
        System.out.format("+-----------------+-----------------+------------------+------------------+-----------------+-----------------+-----------------+%n");

    }

    public Long providePostId() {
        System.out.print("Enter Post ID: ");
        return scanner.nextLong();
    }

    public Label enterLabel() {
        System.out.println("Enter label name: ");
        String name = scanner.nextLine();
        return new Label(null, name);
    }

    public Post updatePost(Post post) {
        System.out.println("Enter new content: ");
        String content = scanner.nextLine();
        List<Label> labels = new ArrayList<>();
        while (true) {
            Label label = enterLabel();
            labels.add(label);
            System.out.println("Do you want to add another label? (y/n)");
            String answer = scanner.nextLine();
            if (answer.equals("n")) {
                break;
            } else if (!answer.equals("y")) {
                System.out.println("Invalid option. Please try again.");
            }
        }
        return new Post(post.id(), content, post.created(), LocalDateTime.now(), labels, post.status(), post.writer());
    }
}
