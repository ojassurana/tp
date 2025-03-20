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

    private InputStream originalSystemIn; // Save original System.in

    @BeforeEach
    void setUp() {
        originalSystemIn = System.in;  // Store the original System.in
    }

    @AfterEach
    void restoreSystemIn() {
        System.setIn(originalSystemIn);  // Restore System.in after each test
    }

    @Test
    public void addTripParsingTest() {
        Map<String, String> parsedCommand;
        String simulatedInput = "add_trip n#2025 Great Barrier Reef d#Summer break with family l#Australia\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));


        parsedCommand = assertDoesNotThrow(Parser::getCommandDetails);


        // Verify the output
        assertEquals("add_trip", parsedCommand.get("command"));
        assertEquals("2025 Great Barrier Reef", parsedCommand.get("name"));
        assertEquals("Summer break with family", parsedCommand.get("description"));
        assertEquals("Australia", parsedCommand.get("location"));
    }

    @Test
    public void addPhotoParsingTest() {
        Map<String, String> parsedCommand;
        String simulatedInput = "add_photo f#./data/photos/sample1.jpg n#Osaka photo c#Friends l#Osaka street\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));


        parsedCommand = assertDoesNotThrow(Parser::getCommandDetails);

        // Verify the output

        assertEquals("add_photo", parsedCommand.get("command"));
        assertEquals("./data/photos/sample1.jpg", parsedCommand.get("filepath"));
        assertEquals("Osaka photo", parsedCommand.get("photoname"));
        assertEquals("Friends", parsedCommand.get("caption"));
        assertEquals("Osaka street", parsedCommand.get("location"));
        assertEquals(5, parsedCommand.keySet().toArray().length); // make sure there are 5 parsed items
    }

    @Test
    public void selectParsingTest() {
        Map<String, String> parsedCommand;
        String simulatedInput = "select 1";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));


        parsedCommand = assertDoesNotThrow(Parser::getCommandDetails);

        // Verify the output

        assertEquals("select", parsedCommand.get("command"));
        assertEquals("1", parsedCommand.get("index"));
    }

    @Test
    public void deleteParsingTest() {
        Map<String, String> parsedCommand;
        String simulatedInput = "delete 10";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));


        parsedCommand = assertDoesNotThrow(Parser::getCommandDetails);

        // Verify the output

        assertEquals("delete", parsedCommand.get("command"));
        assertEquals("10", parsedCommand.get("index"));
    }

    @Test
    public void listParsingTest() {
        Map<String, String> parsedCommand;
        String simulatedInput = "select 1";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));


        parsedCommand = assertDoesNotThrow(Parser::getCommandDetails);


        assertEquals("list", parsedCommand.get("command"));
    }
}
