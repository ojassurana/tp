package command;

import com.drew.imaging.ImageProcessingException;
import exception.MissingCompulsoryParameter;
import exception.NoMetaDataException;
import exception.TravelDiaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trip.Trip;
import trip.TripManager;
import ui.Ui;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AddPhotoCommand following the test case design principles from the NUS SE book:
 * 1. Equivalence Partitioning (EP): Testing representative values from different groups
 * 2. Boundary Value Analysis: Testing edge cases and boundary conditions
 * 3. Positive & Negative Test Cases: Verifying both expected behavior and error handling
 */
public class AddPhotoCommandTest {
    private TripManager tripManager;
    private Ui ui;
    private static final String VALID_FILEPATH = "./data/photos/hongkong_1.jpeg";
    private static final String INVALID_FILEPATH = "./data/photos/nonexistent.jpg";
    private static final String VALID_PHOTONAME = "Test Photo";
    private static final String VALID_CAPTION = "Test Caption";

    @BeforeEach
    void setUp() throws TravelDiaryException {
        tripManager = new TripManager();
        tripManager.setSilentMode(true);
        ui = new Ui();
        
        // Create and select a trip for testing photo addition
        tripManager.addTrip("Test Trip", "Test Trip Description");
        tripManager.selectTrip(0);
    }

    /**
     * Tests for AddPhotoCommand
     * 
     * Equivalence Partitions for filepath:
     * - Valid filepath (existing image file with metadata)
     * - Invalid filepath (non-existent file)
     * 
     * Equivalence Partitions for photoname/caption:
     * - Valid names/captions (non-empty strings)
     * - Invalid names/captions (null)
     */
    @Test
    void testAddPhotoCommand_ValidInputs_ShouldAddPhoto() throws TravelDiaryException, 
            MissingCompulsoryParameter, IOException, ImageProcessingException, NoMetaDataException {
        // Positive test case: Valid inputs
        Command command = new AddPhotoCommand(VALID_FILEPATH, VALID_PHOTONAME, VALID_CAPTION);
        command.execute(tripManager, ui, 1);
        
        // Verify photo was added to the selected trip
        assertEquals(1, tripManager.getSelectedTrip().album.getPhotos().size());
        assertEquals(VALID_PHOTONAME, tripManager.getSelectedTrip().album.getPhotos().get(0).getName());
        assertEquals(VALID_CAPTION, tripManager.getSelectedTrip().album.getPhotos().get(0).getCaption());
    }
    
    @Test
    void testAddPhotoCommand_NullTripManager_ShouldThrowException() {
        // Negative test case: Null TripManager
        Command command = new AddPhotoCommand(VALID_FILEPATH, VALID_PHOTONAME, VALID_CAPTION);
        
        assertThrows(TravelDiaryException.class, () -> 
            command.execute(null, ui, 1)
        );
    }
    
    @Test
    void testAddPhotoCommand_NoSelectedTrip_ShouldThrowException() throws TravelDiaryException {
        // Negative test case: No selected trip
        TripManager emptyTripManager = new TripManager();
        emptyTripManager.setSilentMode(true);
        emptyTripManager.addTrip("Test Trip", "Test Trip Description");
        // Note: No trip selected
        
        Command command = new AddPhotoCommand(VALID_FILEPATH, VALID_PHOTONAME, VALID_CAPTION);
        
        assertThrows(TravelDiaryException.class, () -> 
            command.execute(emptyTripManager, ui, 1)
        );
    }
    
    @Test
    void testAddPhotoCommand_InvalidFilePath_ShouldThrowException() {
        // Negative test case: Invalid file path
        Command command = new AddPhotoCommand(INVALID_FILEPATH, VALID_PHOTONAME, VALID_CAPTION);
        
        assertThrows(IOException.class, () -> 
            command.execute(tripManager, ui, 1)
        );
    }
    
    /**
     * Testing boundary values and combinations:
     * - Adding multiple photos to the same trip
     */
    @Test
    void testAddPhotoCommand_MultiplePhotos_ShouldAddAllPhotos() throws Exception {
        // First photo
        Command command1 = new AddPhotoCommand(VALID_FILEPATH, "Photo 1", "Caption 1");
        command1.execute(tripManager, ui, 1);
        
        // Second photo with same filepath but different name/caption
        Command command2 = new AddPhotoCommand(VALID_FILEPATH, "Photo 2", "Caption 2");
        command2.execute(tripManager, ui, 1);
        
        // Verify both photos were added
        assertEquals(2, tripManager.getSelectedTrip().album.getPhotos().size());
        assertEquals("Photo 1", tripManager.getSelectedTrip().album.getPhotos().get(0).getName());
        assertEquals("Photo 2", tripManager.getSelectedTrip().album.getPhotos().get(1).getName());
    }
} 