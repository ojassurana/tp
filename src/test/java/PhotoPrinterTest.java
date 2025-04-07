import photo.Photo;
import photo.PhotoFrame;
import photo.PhotoPrinter;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhotoPrinterTest {

    @Test
    void createFrame_validPhoto_expectPhotoDetailsToMatchPhotoFrame() {
        String filePath = "./data/photos/hongkong_1.jpeg";
        String photoName = "photo 1 name";
        String caption = "photo 1 caption";
        try {
            Photo photo1 = new Photo(filePath, photoName, caption);
            PhotoFrame photoFrame = PhotoPrinter.createFrame(photo1);
            LocalDateTime dateTime = photo1.getDatetime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");
            String formattedDate = dateTime.format(formatter);
            String location = photo1.getLocation().getLocationName();

            String photoTitle = photoFrame.getTitle();
            String locationDateLabel = photoFrame.getLocationDateLabel().getText();
            System.out.println(photoTitle);
            System.out.println(locationDateLabel);

            assertTrue(photoTitle.contains(photoName), "Photo frame title does not contain photo name.");
            assertEquals(caption, photoFrame.getCaptionLabel().getText());
            assertTrue(locationDateLabel.contains(location),
                    "Photo frame location date label does not contain location name.");
            assertTrue(locationDateLabel.contains(formattedDate));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void createFrame_invalidPhoto_expectFileNotFoundException() {
        try {
            Photo photo1 = new Photo("./data/photos/hongkong_999.jpeg", "photo 1 name", "photo 1 caption");
            assertThrows(FileNotFoundException.class, () -> PhotoPrinter.createFrame(photo1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
