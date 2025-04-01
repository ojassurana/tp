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

    @Test
    public void addTripParsingTest() {
        Map<String, String> parsedCommand = assertDoesNotThrow(() -> Parser.processInput("add_trip n#2025 Great Barrier Reef d#Summer break with family l#Australia"));
        // Verify the parsed result
        assertEquals("add_trip", parsedCommand.get("command"));
        assertEquals("2025 Great Barrier Reef", parsedCommand.get("name"));
        assertEquals("Summer break with family", parsedCommand.get("description"));
        assertEquals("Australia", parsedCommand.get("location"));
    }

    @Test
    public void AddPhotoParsingTest() {
        Map<String, String> parsedCommand = assertDoesNotThrow(() -> Parser.processInput("add_photo f#./data/photos/sample1.jpg n#Osaka photo c#Friends l#Osaka street"));
        // Verify the parsed result
        assertEquals("add_photo", parsedCommand.get("command"));
        assertEquals("./data/photos/sample1.jpg", parsedCommand.get("filepath"));
        assertEquals("Osaka photo", parsedCommand.get("photoname"));
        assertEquals("Friends", parsedCommand.get("caption"));
        assertEquals("Osaka street", parsedCommand.get("location"));
    }

    @Test
    public void SelectParsingTest() {
        Map<String, String> parsedCommand = assertDoesNotThrow(() -> Parser.processInput("select 1"));
        // Verify the parsed result
        assertEquals("select", parsedCommand.get("command"));
        assertEquals("1", parsedCommand.get("index"));
    }

    @Test
    public void DeleteParsingTest() {
        Map<String, String> parsedCommand = assertDoesNotThrow(() -> Parser.processInput("delete 10"));
        // Verify the parsed result
        assertEquals("delete", parsedCommand.get("command"));
        assertEquals("10", parsedCommand.get("index"));
    }

    @Test
    public void ListParsingTest() {
        Map<String, String> parsedCommand = assertDoesNotThrow(() -> Parser.processInput("list"));
        // Verify the parsed result
        assertEquals("list", parsedCommand.get("command"));
    }
}
