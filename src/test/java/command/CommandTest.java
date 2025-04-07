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
 * Contains test cases for various Command implementations.
 * This test class follows the test case design principles from the NUS SE book:
 * 1. Equivalence Partitioning (EP): Testing representative values from different groups
 * 2. Boundary Value Analysis: Testing edge cases and boundary conditions
 * 3. Positive & Negative Test Cases: Verifying both expected behavior and error handling
 */
public class CommandTest {
    private TripManager tripManager;
    private Ui ui;

    /**
     * Sets up the test environment before each test.
     * Initializes a TripManager in silent mode and a UI instance.
     */
    @BeforeEach
    void setUp() {
        tripManager = new TripManager();
        tripManager.setSilentMode(true); // Avoid console output during tests
        ui = new Ui();
    }

    /**
     * Tests that AddTripCommand correctly adds a trip with valid inputs.
     * 
     * @throws Exception if there's an issue with execution
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
    
    /**
     * Tests that AddTripCommand throws TravelDiaryException with null TripManager.
     */
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
     * Tests that ListCommand works with an empty trip list.
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testListCommand_EmptyTripList_ShouldNotThrowException() throws Exception {
        // Positive test case: Empty trip list
        Command command = new ListCommand();
        
        // Should not throw exception when list is empty
        assertDoesNotThrow(() -> command.execute(tripManager, ui, 0));
    }
    
    /**
     * Tests that ListCommand works with existing trips.
     * 
     * @throws Exception if there's an issue with execution
     */
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
     * Tests that SelectCommand correctly selects a trip with valid index.
     * 
     * @throws Exception if there's an issue with execution
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
    
    /**
     * Tests that SelectCommand throws exception with an invalid index.
     * 
     * @throws Exception if there's an issue with execution
     */
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
    
    /**
     * Tests that SelectCommand throws exception with a negative index.
     * 
     * @throws Exception if there's an issue with execution
     */
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
     * Tests that HelpCommand works with null FSM value.
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testHelpCommand_NullFsmValue_ShouldNotThrowException() throws Exception {
        // Positive test case: Null FSM value
        Command command = new HelpCommand(null);
        
        // Should not throw exception
        assertDoesNotThrow(() -> command.execute(tripManager, ui, 0));
    }
    
    /**
     * Tests that HelpCommand works with valid FSM value.
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testHelpCommand_ValidFsmValue_ShouldNotThrowException() throws Exception {
        // Positive test case: Valid FSM value
        Command command = new HelpCommand("0");
        
        // Should not throw exception
        assertDoesNotThrow(() -> command.execute(tripManager, ui, 0));
    }
    
    /**
     * Tests that MenuCommand executes without throwing exceptions.
     * 
     * @throws Exception if there's an issue with execution
     */
    @Test
    void testMenuCommand_ShouldNotThrowException() throws Exception {
        // Positive test case
        Command command = new MenuCommand();
        
        // Should not throw exception
        assertDoesNotThrow(() -> command.execute(tripManager, ui, 1));
    }
    
    /**
     * Tests that ExitCommand correctly returns true for isExit.
     */
    @Test
    void testExitCommand_ShouldReturnTrueForIsExit() {
        // Positive test case
        Command command = new ExitCommand();
        
        // Should return true for isExit()
        assertTrue(command.isExit());
    }
    
    /**
     * Tests that DeleteCommand correctly deletes a trip with valid index.
     * 
     * @throws Exception if there's an issue with execution
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
    
    /**
     * Tests that DeleteCommand throws exception with an invalid index.
     * 
     * @throws Exception if there's an issue with execution
     */
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