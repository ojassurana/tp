package command;

import exception.TravelDiaryException;
import exception.TripNotSelectedException;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contains test cases for the CommandFactory class.
 * Tests follow the principles from the NUS SE book:
 * 1. Equivalence Partitioning: Different types of commands in different FSM states
 * 2. Boundary Value Analysis: Edge cases like invalid commands or state transitions
 * 3. Positive and Negative Test Cases: Valid and invalid inputs
 */
public class CommandFactoryTest {

    /**
     * Tests that ExitCommand is returned for "bye" command in both FSM states.
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testGetCommandExitCommandShouldReturnExitCommand() throws Exception {
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
    
    /**
     * Tests that ListCommand is returned for "list" command in both FSM states.
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testGetCommandListCommandShouldReturnListCommand() throws Exception {
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
    
    /**
     * Tests that SelectCommand is returned for "select" command in both FSM states.
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testGetCommandSelectCommandShouldReturnSelectCommand() throws Exception {
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
    
    /**
     * Tests that DeleteCommand is returned for "delete" command in both FSM states.
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testGetCommandDeleteCommandShouldReturnDeleteCommand() throws Exception {
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
    
    /**
     * Tests that HelpCommand is returned for "help" command in both FSM states.
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testGetCommandHelpCommandShouldReturnHelpCommand() throws Exception {
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
     * Tests that AddTripCommand is returned for "add_trip" command in menu state (FSM=0).
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testGetCommandAddTripCommandShouldReturnAddTripCommand() throws Exception {
        // Positive test case: AddTrip command in menu state
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "add_trip");
        parsedCommand.put("name", "Test Trip");
        parsedCommand.put("description", "Test Description");
        parsedCommand.put("location", "Test Location");
        
        Command command = CommandFactory.getCommand(parsedCommand, 0);
        assertTrue(command instanceof AddTripCommand);
    }
    
    /**
     * Tests that exception is thrown for "menu" command in menu state (FSM=0).
     */
    @Test
    void testGetCommandMenuCommandInMenuStateShouldThrowException() {
        // Negative test case: Menu command in menu state
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "menu");
        
        assertThrows(TravelDiaryException.class, () -> 
            CommandFactory.getCommand(parsedCommand, 0)
        );
    }
    
    /**
     * Tests that AddPhotoCommand is returned for "add_photo" command in trip state (FSM=1).
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testGetCommandAddPhotoCommandShouldReturnAddPhotoCommand() throws Exception {
        // Positive test case: AddPhoto command in trip state
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "add_photo");
        parsedCommand.put("filepath", "./data/photos/hongkong_1.jpg");
        parsedCommand.put("photoname", "Test Photo");
        parsedCommand.put("caption", "Test Caption");
        
        Command command = CommandFactory.getCommand(parsedCommand, 1);
        assertTrue(command instanceof AddPhotoCommand);
    }
    
    /**
     * Tests that MenuCommand is returned for "menu" command in trip state (FSM=1).
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testGetCommandMenuCommandInTripStateShouldReturnMenuCommand() throws Exception {
        // Positive test case: Menu command in trip state
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "menu");
        
        Command command = CommandFactory.getCommand(parsedCommand, 1);
        assertTrue(command instanceof MenuCommand);
    }
    
    /**
     * Tests that exception is thrown for "add_trip" command in trip state (FSM=1).
     */
    @Test
    void testGetCommandAddTripCommandInTripStateShouldThrowException() {
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
    
    /**
     * Tests that exception is thrown for "add_photo" command in menu state (FSM=0).
     */
    @Test
    void testGetCommandAddPhotoCommandInMenuStateShouldThrowException() {
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
     * Tests that exception is thrown for invalid FSM values (not 0 or 1).
     */
    @Test
    void testGetCommandInvalidFsmValueShouldThrowException() {
        // Use a command that is not in the common commands section
        // (not bye, close, select, list, delete, or help)
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "add_trip");
        
        // Invalid FSM value (not 0 or 1) should throw a TravelDiaryException
        TravelDiaryException exception = assertThrows(TravelDiaryException.class, () -> 
            CommandFactory.getCommand(parsedCommand, 999)
        );
        
        // Verify the exception message
        assertEquals("Invalid state.", exception.getMessage());
    }
    
    /**
     * Tests that exception is thrown for invalid commands.
     */
    @Test
    void testGetCommandInvalidCommandShouldThrowException() {
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
    
    /**
     * Tests that exception is thrown when required parameters are missing.
     */
    @Test
    void testGetCommandMissingIndexShouldThrowException() {
        // Negative test case: Missing index for commands that require it
        Map<String, String> parsedCommand = new HashMap<>();
        parsedCommand.put("command", "select");
        // No index provided
        
        assertThrows(NumberFormatException.class, () -> 
            CommandFactory.getCommand(parsedCommand, 0)
        );
    }
} 
