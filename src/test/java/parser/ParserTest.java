package parser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {
    private InputStream originalSystemIn;  // Save original System.in

    @BeforeEach
    void setUp() {
        // Store the original System.in
        originalSystemIn = System.in;
    }

    @AfterEach
    void restoreSystemIn() {
        // Ensure that System.in is restored after each test
        System.setIn(originalSystemIn);
    }

    @Test
    public void addTripParsingTest() {
        // Define the simulated input for the test
        String simulatedInput = "add_trip n#2025 Great Barrier Reef d#Summer break with family l#Australia\n";
        // Set the input stream for the test
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Call the Parser and verify the result
        Map<String, String> parsedCommand = assertDoesNotThrow(Parser::getCommandDetails);

        // Verify the parsed result
        assertEquals("add_trip", parsedCommand.get("command"));
        assertEquals("2025 Great Barrier Reef", parsedCommand.get("name"));
        assertEquals("Summer break with family", parsedCommand.get("description"));
        assertEquals("Australia", parsedCommand.get("location"));
    }

    //    @Test
    //    public void AddPhotoParsingTest() {
    //        String simulatedInput = "add_photo f#./data/photos/sample1.jpg n#Osaka photo c#Friends l#Osaka street\n";
    //        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
    //
    //        Map<String, String> parsedCommand = assertDoesNotThrow(Parser::getCommandDetails);
    //
    //        // Verify the parsed result
    //        assertEquals("add_photo", parsedCommand.get("command"));
    //        assertEquals("./data/photos/sample1.jpg", parsedCommand.get("filepath"));
    //        assertEquals("Osaka photo", parsedCommand.get("photoname"));
    //        assertEquals("Friends", parsedCommand.get("caption"));
    //        assertEquals("Osaka street", parsedCommand.get("location"));
    //    }
    //
    //    @Test
    //    public void SelectParsingTest() {
    //        String simulatedInput = "select 1";
    //        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
    //
    //        Map<String, String> parsedCommand = assertDoesNotThrow(Parser::getCommandDetails);
    //
    //        // Verify the parsed result
    //        assertEquals("select", parsedCommand.get("command"));
    //        assertEquals("1", parsedCommand.get("index"));
    //    }
    //
    //    @Test
    //    public void DeleteParsingTest() {
    //        String simulatedInput = "delete 10";
    //        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
    //
    //        Map<String, String> parsedCommand = assertDoesNotThrow(Parser::getCommandDetails);
    //
    //        // Verify the parsed result
    //        assertEquals("delete", parsedCommand.get("command"));
    //        assertEquals("10", parsedCommand.get("index"));
    //    }
    //
    //    @Test
    //    public void ListParsingTest() {
    //        String simulatedInput = "list";
    //        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
    //
    //        Map<String, String> parsedCommand = assertDoesNotThrow(Parser::getCommandDetails);
    //
    //        // Verify the parsed result
    //        assertEquals("list", parsedCommand.get("command"));
    //    }
}
