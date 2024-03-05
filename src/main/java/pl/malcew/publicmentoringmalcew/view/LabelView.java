package pl.malcew.publicmentoringmalcew.view;

import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Label;

import java.util.List;
import java.util.Scanner;
@Component
public class LabelView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("1. Create label");
        System.out.println("2. Read label");
        System.out.println("3. Update label");
        System.out.println("4. Delete label");
        System.out.println("5. View all labels");
        System.out.println("6. Back to main menu");
    }

    public int readOption() {
        System.out.print("Enter option: ");
        return scanner.nextInt();
    }

    public String provideLabelName() {
        System.out.print("Enter label name: ");
        return scanner.next();
    }

    public Long provideLabelId() {
        System.out.print("Enter label ID: ");
        return scanner.nextLong();
    }

    public Label updateLabel(Label label) {
        System.out.print("Enter new label name: ");
        String name = scanner.next();
        return new Label(label.id(), name);
    }



    public void displayLabels(List<Label> labels) {
        String leftAlignFormat = "| %-15s | %-15s |%n";

        System.out.format("+-----------------+-----------------+%n");
        System.out.format("| ID              | Name            |%n");
        System.out.format("+-----------------+-----------------+%n");
        for (Label label : labels) {
            System.out.format(leftAlignFormat, label.id(), label.name());
        }
        System.out.format("+-----------------+-----------------+%n");
    }
}
