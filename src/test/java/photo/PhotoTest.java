package photo;

import exception.UnsupportedImageFormatException;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the Photo class.
 * Verifies functionality, including metadata extraction and handling of edge cases.
 */
public class PhotoTest {

    /**
     * Tests extracting metadata from a .jpg file.
     * Verifies that the metadata is successfully extracted for the given .jpg file.
     */
    @Test
    void extractData_jpgFile_expectExtractMetadataSuccessfully() {
        try {
            String filePath = "./data/photos/usa/usa1_jpg.jpg";
            String photoName = "USA Photo 1";
            String caption = "Caption for USA Photo 1";

            // Create a Photo object and verify metadata extraction
            Photo photo = new Photo(filePath, photoName, caption);
            assertEquals(photoName, photo.getPhotoName(),
                    "Photo name does not match.");
            assertEquals(caption, photo.getCaption(),
                    "Photo caption does not match.");
            assertEquals(filePath, photo.getFilePath(),
                    "Photo file path does not match.");

            System.out.println(photo.toString()); // Print metadata for verification
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Tests extracting metadata from a .heic file with a .png extension.
     * Verifies that an UnsupportedImageFormatException is thrown for the unsupported file format.
     */
    @Test
    void extractData_pngHeicFile_expectUnsupportedImageFormatException() {
        String filePath = "./data/photos/usa/usa1_png.heic";
        String photoName = "USA Photo 2";
        String caption = "Caption for USA Photo 2";

        assertThrows(UnsupportedImageFormatException.class, () -> {
            new Photo(filePath, photoName, caption);
        });
    }

    /**
     * Tests extracting metadata from a .heic file.
     * Verifies that an UnsupportedImageFormatException is thrown for the unsupported file format.
     */
    @Test
    void extractData_heicFile_expectUnsupportedImageFormatException() {
        String filePath = "./data/photos/usa/usa1_heic.heic";
        String photoName = "USA Photo 3";
        String caption = "Caption for USA Photo 3";

        assertThrows(UnsupportedImageFormatException.class, () -> {
            new Photo(filePath, photoName, caption);
        });
    }

    /**
     * Tests extracting metadata from an empty file path (Boundary Value Analysis).
     * Verifies that an AssertionError is thrown for invalid file paths.
     */
    @Test
    void extractData_emptyFilePath_expectTravelDiaryException() {
        String filePath = "";
        String photoName = "Invalid Photo";
        String caption = "Invalid caption";

        assertThrows(AssertionError.class, () -> {
            new Photo(filePath, photoName, caption);
        });
    }

    /**
     * Tests creating a Photo object with a null file path (Equivalence Partitioning).
     * Verifies that an AssertionError is thrown due to invalid input when the file path is null.
     */
    @Test
    void extractData_nullFilePath_expectTravelDiaryException() {
        String filePath = null;
        String photoName = "Null File Path Photo";
        String caption = "Null File Path Caption";

        assertThrows(AssertionError.class, () -> {
            new Photo(filePath, photoName, caption);
        });
    }

    /**
     * Tests creating a Photo object with special characters in the photo name (Equivalence Partitioning).
     * Verifies that special characters are handled correctly in the photo name.
     */
    @Test
    void extractData_specialCharactersInName_expectPhotoCreatedSuccessfully() {
        try {
            String filePath = "./data/photos/sample_special.jpg";
            String photoName = "@Special#Photo!Name%";
            String caption = "Caption with special characters";

            Photo photo = new Photo(filePath, photoName, caption);

            assertEquals(photoName, photo.getPhotoName(),
                    "Photo name does not match.");
            assertEquals(caption, photo.getCaption(),
                    "Photo caption does not match.");
            assertEquals(filePath, photo.getFilePath(),
                    "File path does not match.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Tests creating a Photo object with valid datetime.
     * Verifies that the datetime is correctly set and retrieved.
     */
    @Test
    void extractData_validDateTime_expectCorrectDateTime() {
        try {
            String filePath = "./data/photos/sample_valid.jpg";
            String photoName = "Valid Datetime Photo";
            String caption = "Photo with valid datetime";
            LocalDateTime validDateTime = LocalDateTime.of(2023, 5, 15, 14, 30);

            Photo photo = new Photo(filePath, photoName, caption, validDateTime);

            assertEquals(validDateTime, photo.getDatetime(),
                    "Datetime does not match the input value.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
