package photo;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
public class PhotoPrinterTest {

    /**
     * Tests creating a photo frame for a valid photo.
     * Verifies that the details in the photo frame match the corresponding photo's attributes.
     */
    @Test
    void createFrame_validPhoto_expectPhotoDetailsToMatchPhotoFrame() {
        String filePath = "./data/photos/hongkong_1.jpeg";
        String photoName = "photo 1 name";
        String caption = "photo 1 caption";
        try {
            Photo photo = new Photo(filePath, photoName, caption);
            PhotoFrame photoFrame = PhotoPrinter.createFrame(photo);
            LocalDateTime dateTime = photo.getDatetime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");
            String formattedDate = dateTime.format(formatter);
            String location = photo.getLocation().getLocationName();

            String photoTitle = photoFrame.getTitle();
            String locationDateLabel = photoFrame.getLocationDateLabel().getText();

            System.out.println(photoTitle);
            System.out.println(locationDateLabel);

            // Verify photo frame contains correct information
            assertTrue(photoTitle.contains(photoName),
                    "Photo frame title does not contain photo name.");
            assertEquals(caption, photoFrame.getCaptionLabel().getText());
            assertTrue(locationDateLabel.contains(location),
                    "Photo frame location/date label does not contain location.");
            assertTrue(locationDateLabel.contains(formattedDate),
                    "Photo frame location/date label does not contain the formatted date.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Tests creating a photo frame for an invalid photo.
     * Verifies that a FileNotFoundException is thrown when the photo file is not found.
     */
    @Test
    void createFrame_invalidPhoto_expectFileNotFoundException() {
        try {
            Photo photo = new Photo("./data/photos/hongkong_999.jpeg", "photo 1 name", "photo 1 caption");
            // Verify that creating a frame for a non-existent photo throws FileNotFoundException
            assertThrows(FileNotFoundException.class, () -> PhotoPrinter.createFrame(photo));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Tests creating a PhotoFrame with a large image file path (Boundary Value Analysis).
     * Verifies that the PhotoPrinter can handle a file with a very long path.
     */
    @Test
    void createFrame_withLongFilePath_expectedCreatePhotoFrameSuccessfully() {
        // 255-character file name (maximum for many file systems)
        String longFilePath = "./data/photos/" + "a".repeat(255) + ".jpg";
        try {
            Photo photo = new Photo(longFilePath, "Long File Path Photo", "Testing long file path photo creation.",
                    LocalDateTime.now());

            assertThrows(FileNotFoundException.class, () -> PhotoPrinter.createFrame(photo),
                    "FileNotFoundException is expected because the file path does not exist.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Tests creating a PhotoFrame with a photo that has a missing datetime (Equivalence Partitioning).
     * Verifies that the PhotoPrinter handles null datetime appropriately by using the current time.
     */
    @Test
    void createFrame_withNullDateTime_expectedUseCurrentTime() {
        try {
            Photo photo = new Photo("./data/photos/sample3.jpg", "Photo Without Datetime",
                    "This photo has no datetime metadata.", null);

            PhotoFrame photoFrame = PhotoPrinter.createFrame(photo);

            assertNotNull(photoFrame, "PhotoFrame is expected to not be null.");
            assertNotNull(photoFrame.getFrame(), "PhotoFrame's JFrame is expected to not be null.");
        } catch (Exception e) {
            System.out.println("Exception is not expected when datetime is null: " + e.getMessage());
        }
    }

    /**
     * Tests creating a PhotoFrame with special characters in the photo name and caption (Equivalence Partitioning).
     * Verifies that special characters are handled correctly.
     */
    @Test
    void createFrame_withSpecialCharactersInDetails_expectedPhotoFrameCreatedSuccessfully() {
        try {
            Photo photo = new Photo("./data/photos/sample4.jpg", "@Special#Photo!Name%",
                    "!Caption_With$Special&Characters*", LocalDateTime.now());

            PhotoFrame photoFrame = PhotoPrinter.createFrame(photo);

            // Assertions
            assertEquals("@Special#Photo!Name%", photoFrame.getTitle(),
                    "PhotoFrame title is expected to match photo name with special characters.");
            assertEquals("!Caption_With$Special&Characters*", photoFrame.getCaptionLabel().getText(),
                    "Caption is expected to match photo caption with special characters.");
        } catch (Exception e) {
            System.out.println("Exception is not expected for special character handling: " + e.getMessage());
        }
    }

    /**
     * Tests closing all photo windows when a single window is open (Boundary Value Analysis).
     * Verifies that a single window is closed successfully without exceptions.
     */
    @Test
    void closeAllWindows_withSingleOpenWindow_expectedCloseSuccessfully() {
        try {
            Photo photo = new Photo("./data/usa/usa1.jpg", "Single Open Window Photo",
                    "This is a test photo for single open window.", LocalDateTime.now());

            // Open one window
            PhotoFrame photoFrame = PhotoPrinter.createFrame(photo);
            PhotoPrinter.display(photoFrame);

            // Close the window
            assertDoesNotThrow(PhotoPrinter::closeAllWindows,
                    "No exception is expected when closing a single open window.");
        } catch (Exception e) {
            System.out.println("Exception is not expected when closing a single window: " + e.getMessage());
        }
    }

    /**
     * Tests creating a PhotoFrame with both null file path and empty photo name (Combining Multiple Test Inputs).
     * Verifies that the assertions and exception handling work as expected.
     */
    @Test
    void createFrame_withNullPathAndEmptyName_expectedAssertionErrors() {
        assertThrows(AssertionError.class, () -> new Photo(null, "", "Empty file path and photo name."),
                "AssertionError is expected for null file path and empty photo name.");
    }

    /**
     * Tests displaying a PhotoFrame created with edge-case datetime formats (Boundary Value Analysis).
     * Verifies that the datetime is formatted and displayed correctly.
     */
    @Test
    void display_withEdgeCaseDateTime_expectedJFrameDisplayedSuccessfully() {
        try {
            LocalDateTime edgeCaseDatetime = LocalDateTime.parse("2000-01-01T00:00:00");
            Photo photo = new Photo("./data/photos/sample6.jpg", "Edge Case Datetime Photo",
                    "Testing edge case datetime handling.", edgeCaseDatetime);

            PhotoFrame photoFrame = PhotoPrinter.createFrame(photo);

            assertDoesNotThrow(() -> PhotoPrinter.display(photoFrame),
                    "No exception is expected when displaying a photo with an edge-case datetime.");
        } catch (Exception e) {
            System.out.println("Exception is not expected for edge-case datetime handling: " + e.getMessage());
        }
    }

    /**
     * Tests creating a PhotoFrame with valid and invalid combinations
     * of photo details(Combining Multiple Test Inputs).
     * Verifies that invalid combinations throw appropriate exceptions while valid combinations succeed.
     */
    @Test
    void createFrame_withValidAndInvalidCombinations_expectedMixedResults() {
        try {
            // Valid photo details
            Photo validPhoto = new Photo("./data/photos/usa/usa1.jpg",
                    "Valid Photo", "This is valid.", LocalDateTime.now());
            assertNotNull(PhotoPrinter.createFrame(validPhoto),
                    "PhotoFrame is expected to be created for valid details.");

            // Invalid photo details (null path)
            assertThrows(AssertionError.class, () -> new Photo(null,
                            "Invalid Photo", "Invalid details."),
                    "AssertionError is expected for null file path.");
        } catch (Exception e) {
            System.out.println("Unexpected exception occurred: " + e.getMessage());
        }
    }
}
