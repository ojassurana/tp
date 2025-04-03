package album;

import exception.InvalidIndexException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

class AlbumTest {

    @Test
    void addPhoto() {
        // Test adding a photo using the overload with datetime
        Album album = new Album();
        LocalDateTime dt = LocalDateTime.of(2023, 1, 1, 12, 0);
        // Removed location parameter from addPhoto call
        assertDoesNotThrow(() -> album.addPhoto("path/to/photo.jpg", "MyPhoto",
                "A beautiful scene", dt));

        // Capture the output from viewPhotos
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        album.viewPhotos();

        System.setOut(originalOut);
        String output = outContent.toString().trim();
        // Expect the photo to be listed with its index and photoName (via Photo.toString)
        assertTrue(output.contains("MyPhoto"), "View photos should list the added photo.");
    }

    @Test
    void testAddPhoto() {
        // Test adding a photo using the overload without specifying a datetime
        Album album = new Album();
        // Removed location parameter from addPhoto call
        assertDoesNotThrow(() -> album.addPhoto("path/to/photo2.jpg", "Photo2", "Sunset"));
        // Capture the output from viewPhotos
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        album.viewPhotos();

        System.setOut(originalOut);
        String output = outContent.toString().trim();
        // Expect the photo to be listed with its index and photoName
        assertTrue(output.contains("Photo2"), "View photos should list the added photo using default " +
                "datetime.");
    }

    @Test
    void deletePhoto() {
        Album album = new Album();
        // Removed location parameter from addPhoto call
        assertDoesNotThrow(() -> album.addPhoto("path/to/photo.jpg", "MyPhoto", "Caption",
                LocalDateTime.of(2023, 1, 1, 12, 0)));
        // Capture output for valid deletion
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        album.deletePhoto(0);

        System.setOut(originalOut);
        String output = outContent.toString().trim();
        assertTrue(output.contains("deleted"), "Photo should be deleted successfully.");

        // Test deletion with an invalid index.
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        album.deletePhoto(0);
        System.setOut(originalOut);
        output = outContent.toString().trim();
        assertTrue(output.contains("Invalid photo index."), "Invalid photo index message should be printed.");
    }

    @Test
    void viewPhotos() {
        Album album = new Album();

        // Test viewPhotos on an empty album
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        album.viewPhotos();
        System.setOut(originalOut);

        String output = outContent.toString().trim();
        assertTrue(output.contains("No photos are found."), "View photos should indicate no photos found.");

        // Test viewPhotos on a non-empty album
        // Removed location parameter from addPhoto calls
        assertDoesNotThrow(() -> album.addPhoto("path/to/photo1.jpg", "Photo1",
                "Caption1", LocalDateTime.of(2023, 2, 2, 15, 0)));
        assertDoesNotThrow(() -> album.addPhoto("path/to/photo2.jpg", "Photo2",
                "Caption2", LocalDateTime.of(2023, 3, 3, 16, 0)));
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        album.viewPhotos();
        System.setOut(originalOut);

        output = outContent.toString().trim();
        assertTrue(output.contains("Photo1"), "First photo should be listed.");
        assertTrue(output.contains("Photo2"), "Second photo should be listed.");
    }

    @Test
    void selectPhoto() {
        Album album = new Album();
        // Removed location parameter from addPhoto call
        assertDoesNotThrow(() -> album.addPhoto("path/to/photo.jpg", "MyPhoto", "Caption",
                LocalDateTime.of(2023, 1, 1, 12, 0)));
        // Capture output for valid photo selection
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        assertDoesNotThrow(() -> album.selectPhoto(0));
        System.setOut(originalOut);
        String output = outContent.toString().trim();
        assertTrue(output.contains("MyPhoto"), "Selected photo details should be printed.");
        assertTrue(output.contains("Caption"), "Selected photo caption should be printed.");
        // Since location is extracted from metadata, we simply check that some location info is printed.
        assertTrue(output.contains("Paris"), "Selected photo location should be printed within parentheses.");

        // Test selecting a photo with an invalid index.
        assertThrows(InvalidIndexException.class, () -> album.selectPhoto(5), "Selecting photo " +
                "with an invalid index should throw an InvalidIndexException.");
    }
}
