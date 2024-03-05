package pl.malcew.publicmentoringmalcew.view;

import org.springframework.stereotype.Component;
import pl.malcew.publicmentoringmalcew.model.Writer;

import java.util.List;
import java.util.Scanner;

@Component
public class WriterView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("1. Create Writer");
        System.out.println("2. Update Writer");
        System.out.println("3. Delete Writer");
        System.out.println("4. View All Writers");
        System.out.println("5. Exit");
    }

    public int readOption() {
        System.out.print("Enter option: ");
        return scanner.nextInt();
    }

    public Long provideWriterId() {
        System.out.print("Enter Writer ID: ");
        return scanner.nextLong();
    }

    public Writer readWriter() {
        System.out.print("Enter Writer First Name: ");
        String firstName = scanner.next();
        System.out.print("Enter Writer Last Name: ");
        String lastName = scanner.next();
        return new Writer(firstName, lastName);
    }

    public Writer updateWriter(Writer writer) {


        System.out.print("\n Enter new Writer First Name: ");
        String firstName = (scanner.next());
        System.out.print("\n Enter new Writer Last Name: ");
        String lastName = (scanner.next());
        return new Writer(writer.id(), firstName, lastName, writer.posts());
    }

    public void displayWriters(List<Writer> writers) {
        String leftAlignFormat = "| %-15s | %-15s |%n";

        System.out.format("+-----------------+-----------------+%n");
        System.out.format("| First Name      | Last Name       |%n");
        System.out.format("+-----------------+-----------------+%n");
        for (Writer writer : writers) {
            System.out.format(leftAlignFormat, writer.firstName(), writer.lastName());
        }
        System.out.format("+-----------------+-----------------+%n");

    }
}