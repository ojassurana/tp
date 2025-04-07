package photo;
import exception.UnsupportedImageFormatException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PhotoTest {

    /**
     * Tests extracting metadata from a .jpg file.
     * Verifies that the metadata is successfully extracted for the given .jpg file.
     */
    @Test
    void extractData_JpgFile_expectExtractMetadataSuccessfully() {
        try {
            String filePath = "./data/photos/usa/usa1_jpg.jpg";
            String photoName = "USA Photo 1";
            String caption = "Caption for USA Photo 1";

            // Create a Photo object and verify metadata extraction
            Photo photo = new Photo(filePath, photoName, caption);
            assertEquals(photoName, photo.getPhotoName(), "Photo name does not match.");
            assertEquals(caption, photo.getCaption(), "Photo caption does not match.");
            assertEquals(filePath, photo.getFilePath(), "Photo file path does not match.");

            System.out.println(photo.toString()); // Print metadata for verification
        } catch (Exception e) {
            System.out.println(e.getMessage()); // Print exception message
        }
    }

    /**
     * Tests extracting metadata from a .heic file with a .png extension.
     * Verifies that an UnsupportedImageFormatException is thrown for the unsupported file format.
     */
    @Test
    void extractData_PngHeicFile_expectUnsupportedImageFormatException() {
        String filePath = "./data/photos/usa/usa1_png.heic";
        String photoName = "USA Photo 2";
        String caption = "Caption for USA Photo 2";

        // Verify that creating a Photo object with an unsupported file format throws an exception
        assertThrows(UnsupportedImageFormatException.class, () -> {
            new Photo(filePath, photoName, caption);
        });
    }

    /**
     * Tests extracting metadata from a .heic file.
     * Verifies that an UnsupportedImageFormatException is thrown for the unsupported file format.
     */
    @Test
    void extractData_wHeicFile_expectUnsupportedImageFormatException() {
        String filePath = "./data/photos/usa/usa1_heic.heic";
        String photoName = "USA Photo 3";
        String caption = "Caption for USA Photo 3";

        // Verify that creating a Photo object with an unsupported file format throws an exception
        assertThrows(UnsupportedImageFormatException.class, () -> {
            new Photo(filePath, photoName, caption);
        });
    }
}
