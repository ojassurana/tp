import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.GraphicsEnvironment;

public class PhotoPrinterTest {

    @Test
    void print_validPhoto_expectPhotoDetailsToMatchPhotoFrame() {
        if (GraphicsEnvironment.isHeadless()) {
            System.setProperty("java.awt.headless", "true");
        }

        LocalDateTime datetime = LocalDateTime.parse("2022-12-23 8:23PM",
                DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma"));
        String filePath = "./data/photos/sample1.jpg";
        String photoName = "First night in Osaka";
        String caption = "This is a photo of my friends and I in Osaka.";
        String location = "Dotonbori River";
        Photo photo = new Photo(filePath, photoName, caption, location, datetime);

        try {
            PhotoFrame photoFrame = PhotoPrinter.print(photo);
            // Check photoName with frame title
            assertEquals(photoName, photoFrame.getTitle());
            // Check caption with frame caption label
            assertEquals(caption, photoFrame.getCaptionLabel().getText());
            // Check location & datetime with frame locationDateLabel
            String expectedLocationDate = location + " | "
                    + datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma"));
            assertEquals(expectedLocationDate, photoFrame.getLocationDateLabel().getText());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void print_invalidPhoto_expectFileNotFoundException() {
        if (GraphicsEnvironment.isHeadless()) {
            System.setProperty("java.awt.headless", "true");
        }

        LocalDateTime datetime = LocalDateTime.parse("2022-12-23 8:23PM",
                DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma"));
        String filePath = "./data/photos/sample0.jpg"; // File does not exist
        String photoName = "First night in Osaka";
        String caption = "This is a photo of my friends and I in Osaka.";
        String location = "Dotonbori River";
        Photo photo = new Photo(filePath, photoName, caption, location, datetime);
        assertThrows(FileNotFoundException.class, () -> PhotoPrinter.print(photo));
    }
}
