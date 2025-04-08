package command;

import exception.DuplicateFilepathException;
import exception.IndexOutOfRangeException;
import exception.InvalidIndexException;
import exception.TravelDiaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import photo.Photo;
import trip.TripManager;
import ui.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testAddTripCommandValidInputsShouldAddTrip() throws Exception {
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
    void testAddTripCommandNullTripManagerShouldThrowException() {
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
    void testListCommandEmptyTripListShouldNotThrowException() throws Exception {
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
    void testListCommandWithExistingTripsShouldNotThrowException() throws Exception {
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
    void testSelectCommandValidIndexShouldSelectTrip() throws Exception {
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
    void testSelectCommandInvalidIndexShouldThrowException() throws Exception {
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
    void testSelectCommandNegativeIndexShouldThrowException() throws Exception {
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
    void testHelpCommandNullFsmValueShouldNotThrowException() throws Exception {
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
    void testHelpCommandValidFsmValueShouldNotThrowException() throws Exception {
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
    void testMenuCommandShouldNotThrowException() throws Exception {
        // Positive test case
        Command command = new MenuCommand();
        
        // Should not throw exception
        assertDoesNotThrow(() -> command.execute(tripManager, ui, 1));
    }
    
    /**
     * Tests that ExitCommand correctly returns true for isExit.
     */
    @Test
    void testExitCommandShouldReturnTrueForIsExit() {
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
    void testDeleteCommandValidIndexShouldDeleteTrip() throws Exception {
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
    void testDeleteCommandInvalidIndexShouldThrowException() throws Exception {
        // Negative test case: Invalid index
        tripManager.addTrip("Trip 1", "Description 1");
        int invalidIndex = 5; // Out of bounds
        
        Command command = new DeleteCommand(invalidIndex);
        
        // Should throw exception for invalid index
        assertThrows(IndexOutOfRangeException.class, () -> 
            command.execute(tripManager, ui, 0)
        );
    }

    @Test
    void testAddTripSelectTripAddPhotoSelectPhoto() throws Exception {
        // Positive test case: Valid inputs
        String name = "Japan Trip";
        String description = "Skiing in Hokkaido";
        String location = "Japan";
        int fsmValue = 0;
        String filepath = "./data/photos/group_photo.jpg";
        String photoname = "street photo";
        String caption = "final day";


        Command command = new AddTripCommand(name, description, location);
        command.execute(tripManager, ui, 0);

        command = new SelectCommand(0);
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;

        command = new AddPhotoCommand(filepath,photoname,caption);
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;

        command = new SelectCommand(0);
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;

        command = new ExitCommand();
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;


        // Verify trip was added

        assertEquals(1, tripManager.getTrips().size());
        assertEquals(name, tripManager.getTrips().get(0).name);
        assertEquals(description, tripManager.getTrips().get(0).description);
        assertEquals(1, tripManager.getTrips().get(0).album.photos.size());
        assertEquals(photoname, tripManager.getTrips().get(0).album.photos.get(0).getPhotoName());
        assertEquals("Tokyo, Japan", tripManager.getTrips().get(0).album.photos.get(0).getLocation().getLocationName());
    }

    @Test
    void testWrongFsm() throws Exception {
        // Positive test case: Valid inputs
        String name = "Japan Trip";
        String description = "Skiing in Hokkaido";
        String location = "Japan";
        int fsmValue = 0;
        String filepath = "./data/photos/group_photo.jpg";
        String photoname = "street photo";
        String caption = "final day";


        Command command = new AddTripCommand(name, description, location);
        command.execute(tripManager, ui, 0);
        fsmValue = command.fsmValue;


        command = new AddPhotoCommand(filepath,photoname,caption);
        Command finalCommand = command;
        int finalFsmValue = fsmValue;
        assertThrows(java.lang.AssertionError.class, () ->
                finalCommand.execute(tripManager, ui, finalFsmValue));
    }

    @Test
    void testAddTrip2PhotoOrder() throws Exception {
        // Positive test case: Valid inputs
        String name = "Japan Trip";
        String description = "Skiing in Hokkaido";
        String location = "Japan";
        int fsmValue = 0;
        String filepath = "./data/photos/group_photo.jpg";
        String photoname = "street photo";
        String caption = "final day";
        String filepath1 = "./data/photos/samurai.jpg";
        String photoname1 = "museum";
        String caption1 = "cool";


        Command command = new AddTripCommand(name, description, location);
        command.execute(tripManager, ui, 0);

        command = new SelectCommand(0);
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;

        command = new AddPhotoCommand(filepath,photoname,caption);
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;

        command = new AddPhotoCommand(filepath1,photoname1,caption1);
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;

        command = new ExitCommand();
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;

        assertEquals(2, tripManager.getTrips().get(0).album.photos.size());
        assertEquals(photoname, tripManager.getTrips().get(0).album.photos.get(0).getPhotoName());
        assertEquals(caption, tripManager.getTrips().get(0).album.photos.get(0).getCaption());
        assertEquals(photoname1, tripManager.getTrips().get(0).album.photos.get(1).getPhotoName());
        assertEquals(caption1, tripManager.getTrips().get(0).album.photos.get(1).getCaption());
        command = new MenuCommand();
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;
        command = new DeleteCommand(0);
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;
        assertEquals(0, tripManager.getTrips().size());
    }

    @Test
    void testDuplicateTripName() throws Exception {
        String name = "Japan Trip";
        String description = "Skiing in Hokkaido";
        String location = "Japan";
        int fsmValue = 0;


        Command command = new AddTripCommand(name, description, location);
        command.execute(tripManager, ui, 0);
        fsmValue = command.fsmValue;


        command = new AddTripCommand(name, description, location);
        int finalFsmValue = fsmValue;
        Command finalCommand = command;
        assertThrows(exception.DuplicateNameException.class, () ->
                finalCommand.execute(tripManager, ui, finalFsmValue));;

    }

    @Test
    void test2PhotoFilepathOrder() throws Exception {
        // Positive test case: Valid inputs
        String name = "Japan Trip";
        String description = "Skiing in Hokkaido";
        String location = "Japan";
        int fsmValue = 0;
        String filepath = "./data/photos/group_photo.jpg";
        String photoname = "street photo";
        String caption = "final day";


        Command command = new AddTripCommand(name, description, location);
        command.execute(tripManager, ui, 0);

        command = new SelectCommand(0);
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;

        command = new AddPhotoCommand(filepath,photoname,caption);
        command.execute(tripManager, ui, fsmValue);
        fsmValue = command.fsmValue;

        command = new AddPhotoCommand(filepath,photoname + "1",caption + " 1");
        Command finalCommand = command;
        int finalFsmValue = fsmValue;
        assertThrows(DuplicateFilepathException.class, () -> finalCommand.execute(tripManager,ui, finalFsmValue));


    }


}
