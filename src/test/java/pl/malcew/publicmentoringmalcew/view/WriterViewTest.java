package pl.malcew.publicmentoringmalcew.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.malcew.publicmentoringmalcew.model.Writer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WriterViewTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void displayWriters() {
        WriterView writerView = new WriterView();
        Writer writer1 = new Writer("John", "Doe");
        Writer writer2 = new Writer("Jane", "Doe");

        writerView.displayWriters(Arrays.asList(writer1, writer2));

        String expectedOutput1 = "| John            | Doe             |";
        String expectedOutput2 = "| Jane            | Doe             |";

        assertTrue(outContent.toString().contains(expectedOutput1));
        assertTrue(outContent.toString().contains(expectedOutput2));
    }
}