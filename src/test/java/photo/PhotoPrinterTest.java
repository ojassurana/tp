package photo;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
            assertTrue(photoTitle.contains(photoName), "Photo frame title does not contain photo name.");
            assertEquals(caption, photoFrame.getCaptionLabel().getText());
            assertTrue(locationDateLabel.contains(location), "Photo frame location/date label does not contain location.");
            assertTrue(locationDateLabel.contains(formattedDate), "Photo frame location/date label does not contain the formatted date.");
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
}
