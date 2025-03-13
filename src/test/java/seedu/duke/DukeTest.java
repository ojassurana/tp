package seedu.duke;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DukeTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream)); // Redirects System.out to outputStream
    }

    @Test
    void duke_runWithNameInput_printsCorrectGreeting() {
        String simulatedInput = "Alice\n"; // Simulating user typing "Alice" and pressing Enter
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes())); // Redirect System.in

        // Run main() to capture output
        Duke.main(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("Hello Alice"), "Expected output to contain 'Hello Alice'");
    }

    @Test
    void duke_runWithEmptyInput_printsHelloBlank() {
        String simulatedInput = "\n"; // Simulating user pressing Enter without input
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Duke.main(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("Hello "), "Expected output to contain 'Hello ' even if input is empty.");
    }
}
