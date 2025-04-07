package command;

import exception.TravelDiaryException;
import exception.TripNotSelectedException;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CommandFactory class following the test case design principles:
 * 1. Equivalence Partitioning: Different types of commands in different FSM states
 * 2. Boundary Value Analysis: Edge cases like invalid commands or state transitions
 * 3. Positive and Negative Test Cases: Valid and invalid inputs
 */
public class CommandFactoryTest {

    /**
     * Tests for commands available in both FSM states
     */
    @Test
    void testGetCommand_ExitCommand_ShouldReturnExitCommand() throws Exception {
        // Positive test case: Exit command in menu state
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "bye");
        
        Command command = CommandFactory.getCommand(parsedCommand, 0);
        assertTrue(command instanceof ExitCommand);
        assertTrue(command.isExit());
        
        // Exit command in trip state
        command = CommandFactory.getCommand(parsedCommand, 1);
        assertTrue(command instanceof ExitCommand);
        assertTrue(command.isExit());
    }
    
    @Test
    void testGetCommand_ListCommand_ShouldReturnListCommand() throws Exception {
        // Positive test case: List command in both states
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "list");
        
        // In menu state
        Command command = CommandFactory.getCommand(parsedCommand, 0);
        assertTrue(command instanceof ListCommand);
        
        // In trip state
        command = CommandFactory.getCommand(parsedCommand, 1);
        assertTrue(command instanceof ListCommand);
    }
    
    @Test
    void testGetCommand_SelectCommand_ShouldReturnSelectCommand() throws Exception {
        // Positive test case: Select command in both states
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "select");
        parsedCommand.put("index", "1");
        
        // In menu state
        Command command = CommandFactory.getCommand(parsedCommand, 0);
        assertTrue(command instanceof SelectCommand);
        
        // In trip state
        command = CommandFactory.getCommand(parsedCommand, 1);
        assertTrue(command instanceof SelectCommand);
    }
    
    @Test
    void testGetCommand_DeleteCommand_ShouldReturnDeleteCommand() throws Exception {
        // Positive test case: Delete command in both states
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "delete");
        parsedCommand.put("index", "1");
        
        // In menu state
        Command command = CommandFactory.getCommand(parsedCommand, 0);
        assertTrue(command instanceof DeleteCommand);
        
        // In trip state
        command = CommandFactory.getCommand(parsedCommand, 1);
        assertTrue(command instanceof DeleteCommand);
    }
    
    @Test
    void testGetCommand_HelpCommand_ShouldReturnHelpCommand() throws Exception {
        // Positive test case: Help command in both states
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "help");
        
        // In menu state
        Command command = CommandFactory.getCommand(parsedCommand, 0);
        assertTrue(command instanceof HelpCommand);
        
        // In trip state
        command = CommandFactory.getCommand(parsedCommand, 1);
        assertTrue(command instanceof HelpCommand);
    }
    
    /**
     * Tests for menu state specific commands
     */
    @Test
    void testGetCommand_AddTripCommand_ShouldReturnAddTripCommand() throws Exception {
        // Positive test case: AddTrip command in menu state
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "add_trip");
        parsedCommand.put("name", "Test Trip");
        parsedCommand.put("description", "Test Description");
        parsedCommand.put("location", "Test Location");
        
        Command command = CommandFactory.getCommand(parsedCommand, 0);
        assertTrue(command instanceof AddTripCommand);
    }
    
    @Test
    void testGetCommand_MenuCommandInMenuState_ShouldThrowException() {
        // Negative test case: Menu command in menu state
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "menu");
        
        assertThrows(TravelDiaryException.class, () -> 
            CommandFactory.getCommand(parsedCommand, 0)
        );
    }
    
    /**
     * Tests for trip state specific commands
     */
    @Test
    void testGetCommand_AddPhotoCommand_ShouldReturnAddPhotoCommand() throws Exception {
        // Positive test case: AddPhoto command in trip state
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "add_photo");
        parsedCommand.put("filepath", "./data/photos/hongkong_1.jpg");
        parsedCommand.put("photoname", "Test Photo");
        parsedCommand.put("caption", "Test Caption");
        
        Command command = CommandFactory.getCommand(parsedCommand, 1);
        assertTrue(command instanceof AddPhotoCommand);
    }
    
    @Test
    void testGetCommand_MenuCommandInTripState_ShouldReturnMenuCommand() throws Exception {
        // Positive test case: Menu command in trip state
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "menu");
        
        Command command = CommandFactory.getCommand(parsedCommand, 1);
        assertTrue(command instanceof MenuCommand);
    }
    
    @Test
    void testGetCommand_AddTripCommandInTripState_ShouldThrowException() {
        // Negative test case: AddTrip command in trip state
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "add_trip");
        parsedCommand.put("name", "Test Trip");
        parsedCommand.put("description", "Test Description");
        parsedCommand.put("location", "Test Location");
        
        assertThrows(TravelDiaryException.class, () -> 
            CommandFactory.getCommand(parsedCommand, 1)
        );
    }
    
    @Test
    void testGetCommand_AddPhotoCommandInMenuState_ShouldThrowException() {
        // Negative test case: AddPhoto command in menu state
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "add_photo");
        parsedCommand.put("filepath", "./data/photos/hongkong_1.jpg");
        parsedCommand.put("photoname", "Test Photo");
        parsedCommand.put("caption", "Test Caption");
        
        assertThrows(TripNotSelectedException.class, () -> 
            CommandFactory.getCommand(parsedCommand, 0)
        );
    }
    
    /**
     * Boundary value tests
     */
    @Test
    void testGetCommand_InvalidFsmValue_ShouldThrowException() {
        // Test case for "list" command with an invalid FSM value
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "list");
        
        // From the CommandFactory implementation, an invalid state (not 0 or 1)
        // should throw a TravelDiaryException
        TravelDiaryException exception = assertThrows(TravelDiaryException.class, () -> 
            CommandFactory.getCommand(parsedCommand, 999) // Very large FSM value
        );
        
        // Verify the exception message
        assertEquals("Invalid state.", exception.getMessage());
    }
    
    @Test
    void testGetCommand_InvalidCommand_ShouldThrowException() {
        // Negative test case: Invalid command
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "invalid_command");
        
        assertThrows(TripNotSelectedException.class, () -> 
            CommandFactory.getCommand(parsedCommand, 0)
        );
        
        assertThrows(TravelDiaryException.class, () -> 
            CommandFactory.getCommand(parsedCommand, 1)
        );
    }
    
    @Test
    void testGetCommand_MissingIndex_ShouldThrowException() {
        // Negative test case: Missing index for commands that require it
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "select");
        // No index provided
        
        assertThrows(NumberFormatException.class, () -> 
            CommandFactory.getCommand(parsedCommand, 0)
        );
    }
} 