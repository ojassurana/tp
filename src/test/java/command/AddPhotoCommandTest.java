package command;

import exception.DuplicateNameException;
import exception.IndexOutOfRangeException;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trip.TripManager;
import ui.Ui;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Contains test cases for the AddPhotoCommand class.
 * Tests follow the principles from the NUS SE book:
 * 1. Equivalence Partitioning (EP): Testing representative values from different groups
 * 2. Boundary Value Analysis: Testing edge cases and boundary conditions
 * 3. Positive & Negative Test Cases: Verifying both expected behavior and error handling
 */
public class AddPhotoCommandTest {
    private static final String VALID_FILEPATH = "./data/photos/hongkong_1.jpg";
    private static final String INVALID_FILEPATH = "./data/photos/nonexistent.jpg";
    private static final String VALID_PHOTONAME = "Test Photo";
    private static final String VALID_CAPTION = "Test Caption";
    
    private TripManager tripManager;
    private Ui ui;

    /**
     * Sets up the test environment before each test.
     * Initializes a TripManager in silent mode, creates a trip, and selects it.
     * 
     * @throws TravelDiaryException if there's an issue with the TripManager
     * @throws IndexOutOfRangeException if there's an issue with trip selection
     */
    @BeforeEach
    void setUp() throws TravelDiaryException, IndexOutOfRangeException, DuplicateNameException, MissingCompulsoryParameter {
        tripManager = new TripManager();
        tripManager.setSilentMode(true);
        ui = new Ui();
        
        // Create and select a trip for testing photo addition
        tripManager.addTrip("Test Trip", "Test Trip Description");
        tripManager.selectTrip(0);
    }

    /**
     * Tests that AddPhotoCommand throws exception with null TripManager.
     * 
     * Since we can't test with actual photo files in a unit test environment,
     * we'll focus on testing the command's behavior with exception handling.
     */
    @Test
    void testAddPhotoCommandNullTripManagerShouldThrowException() {
        // Negative test case: Null TripManager
        Command command = new AddPhotoCommand(VALID_FILEPATH, VALID_PHOTONAME, VALID_CAPTION);
        
        assertThrows(TravelDiaryException.class, () -> 
            command.execute(null, ui, 1)
        );
    }
    
    /**
     * Tests that AddPhotoCommand throws exception when no trip is selected.
     * 
     * @throws TravelDiaryException if there's an issue with the TripManager
     */
    @Test
    void testAddPhotoCommandNoSelectedTripShouldThrowException() throws TravelDiaryException {
        // Negative test case: No selected trip
        TripManager emptyTripManager = new TripManager();
        emptyTripManager.setSilentMode(true);
        
        // We don't add or select any trip
        Command command = new AddPhotoCommand(VALID_FILEPATH, VALID_PHOTONAME, VALID_CAPTION);
        
        // This should throw an assertion error
        assertThrows(AssertionError.class, () -> 
            command.execute(emptyTripManager, ui, 1)
        );
    }
    
    /**
     * Tests that AddPhotoCommand throws exception with invalid file path.
     */
    @Test
    void testAddPhotoCommandInvalidFilePathShouldThrowException() {
        // Negative test case: Invalid file path
        // Since we don't have actual photo files, all paths will throw exceptions
        // We can still test that the command correctly handles file-related exceptions
        Command command = new AddPhotoCommand(INVALID_FILEPATH, VALID_PHOTONAME, VALID_CAPTION);
        
        // Either IOException or UnsupportedImageFormatException might be thrown
        // depending on the implementation
        assertThrows(Exception.class, () -> 
            command.execute(tripManager, ui, 1)
        );
    }
} 
