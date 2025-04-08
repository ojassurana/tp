package parser;

import exception.TravelDiaryException;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    public void addTripParsingTest() {
        Map<String, String> parsedCommand = assertDoesNotThrow(() ->
                Parser.processInput("add_trip n#2025 Great Barrier Reef d#Summer break with family"));
        // Verify the parsed result
        assertEquals("add_trip", parsedCommand.get("command"));
        assertEquals("2025 Great Barrier Reef", parsedCommand.get("name"));
        assertEquals("Summer break with family", parsedCommand.get("description"));
    }

    @Test
    public void addPhotoParsingTest() {
        // Remove the location tag from the command since it's no longer required
        Map<String, String> parsedCommand = assertDoesNotThrow(() ->
                Parser.processInput("add_photo f#./data/photos/sample1.jpg n#Osaka photo c#Friends"));
        // Verify the parsed result
        assertEquals("add_photo", parsedCommand.get("command"));
        assertEquals("./data/photos/sample1.jpg", parsedCommand.get("filepath"));
        assertEquals("Osaka photo", parsedCommand.get("photoname"));
        assertEquals("Friends", parsedCommand.get("caption"));
        // Location is optional and should be absent or null.
        assertEquals(null, parsedCommand.get("location"));
    }

    @Test
    public void selectParsingTest() {
        Map<String, String> parsedCommand = assertDoesNotThrow(() -> Parser.processInput("select 1"));
        // Verify the parsed result
        assertEquals("select", parsedCommand.get("command"));
        assertEquals("1", parsedCommand.get("index"));
    }

    @Test
    public void deleteParsingTest() {
        Map<String, String> parsedCommand = assertDoesNotThrow(() -> Parser.processInput("delete 10"));
        // Verify the parsed result
        assertEquals("delete", parsedCommand.get("command"));
        assertEquals("10", parsedCommand.get("index"));
    }

    @Test
    public void listParsingTest() {
        Map<String, String> parsedCommand = assertDoesNotThrow(() -> Parser.processInput("list"));
        // Verify the parsed result
        assertEquals("list", parsedCommand.get("command"));
    }

    @Test
    public void UnrecogniseParsing() {
        assertThrows(exception.CommandNotRecogniseException.class, () ->
                Parser.processInput("bla"));
    }

    public void AddTripParsingTest() {
        Map<String, String> parsedCommand = assertDoesNotThrow(() ->
                Parser.processInput("add_trip n#2025 Great Barrier Reef d#Summer break with family"));
        // Verify the parsed result
        assertEquals("add_trip", parsedCommand.get("command"));
        assertEquals("2025 Great Barrier Reef", parsedCommand.get("name"));
        assertEquals("Summer break with family", parsedCommand.get("description"));
    }
    @Test
    public void addMissingTagParsingTest() {
        assertThrows (exception.MissingTagsException.class,() ->
                Parser.processInput("add_trip  d#Summer break with family"));
    }

    @Test
    public void addDuplicateTagParsingTest() {
        assertThrows (exception.TagException.class,() ->
                Parser.processInput("add_trip  d#Summer break with family d#Summer break with family"));
    }



}
