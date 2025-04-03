import photo.Photo;
import photo.PhotoFrame;
import photo.PhotoPrinter;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PhotoPrinterTest {

    @Test
    void createFrame_validPhoto_expectPhotoDetailsToMatchPhotoFrame() {
        LocalDateTime datetime = LocalDateTime.parse("2022-12-23 8:23PM",
                DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH));
        String filePath = "./data/photos/sample1.jpg";
        String photoName = "First night in Osaka";
        String caption = "This is a photo of my friends and I in Osaka.";
        // Call the Photo constructor without the location parameter.
        Photo photo = assertDoesNotThrow(() -> new Photo(filePath, photoName, caption, datetime));

        try {
            PhotoFrame photoFrame = PhotoPrinter.createFrame(photo);
            // Check photoName with frame title
            assertEquals(photoName, photoFrame.getTitle());
            // Check caption with frame caption label
            assertEquals(caption, photoFrame.getCaptionLabel().getText());
            // Since location is extracted from metadata, we cannot assert an exact value.
            // Instead, verify that the location/date label contains the formatted datetime.
            String formattedDate = datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma"));
            assertEquals(true, photoFrame.getLocationDateLabel().getText().contains(formattedDate));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void createFrame_invalidPhoto_expectFileNotFoundException() {
        LocalDateTime datetime = LocalDateTime.parse("2022-12-23 8:23PM",
                DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH));
        String filePath = "./data/photos/sample0.jpg"; // File does not exist
        String photoName = "First night in Osaka";
        String caption = "This is a photo of my friends and I in Osaka.";
        Photo photo = assertDoesNotThrow(() -> new Photo(filePath, photoName, caption, datetime));
        assertThrows(FileNotFoundException.class, () -> PhotoPrinter.createFrame(photo));
    }
}
