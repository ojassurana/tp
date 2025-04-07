package command;

import exception.IndexOutOfRangeException;
import exception.InvalidIndexException;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trip.Trip;
import trip.TripManager;
import ui.Ui;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class follows the test case design principles from the NUS SE book:
 * 1. Equivalence Partitioning (EP): Testing representative values from different groups
 * 2. Boundary Value Analysis: Testing edge cases and boundary conditions
 * 3. Positive & Negative Test Cases: Verifying both expected behavior and error handling
 */
public class CommandTest {
    private TripManager tripManager;
    private Ui ui;

    @BeforeEach
    void setUp() {
        tripManager = new TripManager();
        tripManager.setSilentMode(true); // Avoid console output during tests
        ui = new Ui();
    }

    /**
     * Tests for AddTripCommand
     * 
     * Equivalence Partitions for name:
     * - Valid names (non-empty strings)
     * - Invalid names (null)
     * 
     * Equivalence Partitions for description:
     * - Valid descriptions (non-empty strings)
     * - Invalid descriptions (null)
     */
    @Test
    void testAddTripCommand_ValidInputs_ShouldAddTrip() throws Exception {
        // Positive test case: Valid inputs
        String name = "Japan Trip";
        String description = "Skiing in Hokkaido";
        String location = "Japan";
        
        Command command = new AddTripCommand(name, description, location);
        command.execute(tripManager, ui, 0);
        
        // Verify trip was added
        assertEquals(1, tripManager.getTrips().size());
        assertEquals(name, tripManager.getTrips().get(0).name);
        assertEquals(description, tripManager.getTrips().get(0).description);
    }
    
    @Test
    void testAddTripCommand_NullTripManager_ShouldThrowException() {
        // Negative test case: Null TripManager
        String name = "Japan Trip";
        String description = "Skiing in Hokkaido";
        String location = "Japan";
        
        Command command = new AddTripCommand(name, description, location);
        
        assertThrows(TravelDiaryException.class, () -> 
            command.execute(null, ui, 0)
        );
    }
    
    /**
     * Tests for ListCommand
     */
    @Test
    void testListCommand_EmptyTripList_ShouldNotThrowException() throws Exception {
        // Positive test case: Empty trip list
        Command command = new ListCommand();
        
        // Should not throw exception when list is empty
        assertDoesNotThrow(() -> command.execute(tripManager, ui, 0));
    }
    
    @Test
    void testListCommand_WithExistingTrips_ShouldNotThrowException() throws Exception {
        // Positive test case: With existing trips
        // First add some trips
        tripManager.addTrip("Trip 1", "Description 1");
        tripManager.addTrip("Trip 2", "Description 2");
        
        Command command = new ListCommand();
        
        // Should not throw exception
        assertDoesNotThrow(() -> command.execute(tripManager, ui, 0));
    }
    
    /**
     * Tests for SelectCommand
     * 
     * Equivalence Partitions for index:
     * - Valid index (within bounds of trip list)
     * - Invalid index (negative or >= size of trip list)
     */
    @Test
    void testSelectCommand_ValidIndex_ShouldSelectTrip() throws Exception {
        // Positive test case: Valid index
        tripManager.addTrip("Trip 1", "Description 1");
        int validIndex = 0; // Zero-based index
        
        Command command = new SelectCommand(validIndex);
        command.execute(tripManager, ui, 0);
        
        // Verify the trip was selected
        assertNotNull(tripManager.getSelectedTrip());
        assertEquals("Trip 1", tripManager.getSelectedTrip().name);
    }
    
    @Test
    void testSelectCommand_InvalidIndex_ShouldThrowException() throws Exception {
        // Negative test case: Invalid index - out of bounds
        tripManager.addTrip("Trip 1", "Description 1");
        int invalidIndex = 5; // Out of bounds
        
        Command command = new SelectCommand(invalidIndex);
        
        // Should throw exception for invalid index
        assertThrows(InvalidIndexException.class, () -> 
            command.execute(tripManager, ui, 0)
        );
    }
    
    @Test
    void testSelectCommand_NegativeIndex_ShouldThrowException() throws Exception {
        // Negative test case: Invalid index - negative
        tripManager.addTrip("Trip 1", "Description 1");
        int invalidIndex = -1; // Negative index
        
        Command command = new SelectCommand(invalidIndex);
        
        // Should throw exception for negative index
        assertThrows(InvalidIndexException.class, () -> 
            command.execute(tripManager, ui, 0)
        );
    }
    
    /**
     * Tests for HelpCommand
     */
    @Test
    void testHelpCommand_NullFsmValue_ShouldNotThrowException() throws Exception {
        // Positive test case: Null FSM value
        Command command = new HelpCommand(null);
        
        // Should not throw exception
        assertDoesNotThrow(() -> command.execute(tripManager, ui, 0));
    }
    
    @Test
    void testHelpCommand_ValidFsmValue_ShouldNotThrowException() throws Exception {
        // Positive test case: Valid FSM value
        Command command = new HelpCommand("0");
        
        // Should not throw exception
        assertDoesNotThrow(() -> command.execute(tripManager, ui, 0));
    }
    
    /**
     * Tests for MenuCommand
     */
    @Test
    void testMenuCommand_ShouldNotThrowException() throws Exception {
        // Positive test case
        Command command = new MenuCommand();
        
        // Should not throw exception
        assertDoesNotThrow(() -> command.execute(tripManager, ui, 1));
    }
    
    /**
     * Tests for ExitCommand
     */
    @Test
    void testExitCommand_ShouldReturnTrueForIsExit() {
        // Positive test case
        Command command = new ExitCommand();
        
        // Should return true for isExit()
        assertTrue(command.isExit());
    }
    
    /**
     * Tests for DeleteCommand
     * 
     * Equivalence Partitions for index:
     * - Valid index (within bounds of trip list)
     * - Invalid index (negative or >= size of trip list)
     */
    @Test
    void testDeleteCommand_ValidIndex_ShouldDeleteTrip() throws Exception {
        // Positive test case: Valid index
        tripManager.addTrip("Trip 1", "Description 1");
        tripManager.addTrip("Trip 2", "Description 2");
        int validIndex = 0; // Zero-based index
        
        Command command = new DeleteCommand(validIndex);
        command.execute(tripManager, ui, 0);
        
        // Verify the trip was deleted
        assertEquals(1, tripManager.getTrips().size());
        assertEquals("Trip 2", tripManager.getTrips().get(0).name);
    }
    
    @Test
    void testDeleteCommand_InvalidIndex_ShouldThrowException() throws Exception {
        // Negative test case: Invalid index
        tripManager.addTrip("Trip 1", "Description 1");
        int invalidIndex = 5; // Out of bounds
        
        Command command = new DeleteCommand(invalidIndex);
        
        // Should throw exception for invalid index
        assertThrows(IndexOutOfRangeException.class, () -> 
            command.execute(tripManager, ui, 0)
        );
    }
} 