//package storage;
//
//import album.Album;
//import com.drew.imaging.ImageProcessingException;
//import exception.FileWriteException;
//import exception.TravelDiaryException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.io.TempDir;
//import photo.Location;
//import photo.Photo;
//import trip.Trip;
//
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.fail;
//
//class StorageWriterTest {
//    @TempDir
//    Path tempDir;
//
//    private List<Trip> trips;
//    private String testFilePath;
//
//    @BeforeEach
//    void setUp() throws TravelDiaryException, ImageProcessingException, IOException {
//        trips = new ArrayList<>();
//        testFilePath = tempDir.resolve("test_trips.dat").toString();
//
//        // Create test trips
//        Trip trip1 = new Trip("Test Trip 1", "Test Description 1");
//        trip1.album = new Album();
//
//        Trip trip2 = new Trip("Test Trip 2", "Test Description 2");
//        trip2.album = new Album();
//
//        // Create photos using our test method that avoids real metadata extraction
//        LocalDateTime photoTime = LocalDateTime.of(2023, 1, 1, 12, 0);
//
//        Photo photo1 = createTestPhoto(createTestPhotoFile("photo1.jpg"), "Photo 1", "Caption 1", photoTime);
//        trip1.album.photos.add(photo1);
//
//        Photo photo2 = createTestPhoto(createTestPhotoFile("photo2.jpg"), "Photo 2", "Caption 2", photoTime);
//        trip2.album.photos.add(photo2);
//
//        trips.add(trip1);
//        trips.add(trip2);
//    }
//
//    // Helper method to create test photos without real metadata extraction
//    private Photo createTestPhoto(String filePath, String photoName, String caption, LocalDateTime datetime)
//            throws TravelDiaryException {
//        try {
//            // Create a photo with reflection to bypass metadata extraction
//            Photo photo = new Photo(filePath, photoName, caption);
//
//            // Set the datetime and location fields directly using reflection
//            Field datetimeField = Photo.class.getDeclaredField("datetime");
//            datetimeField.setAccessible(true);
//            datetimeField.set(photo, datetime);
//
//            Field locationField = Photo.class.getDeclaredField("location");
//            locationField.setAccessible(true);
//            locationField.set(photo, new Location(1.0, 2.0, "Location 1"));
//
//            Field locationNameField = Photo.class.getDeclaredField("locationName");
//            locationNameField.setAccessible(true);
//            locationNameField.set(photo, "Location 1");
//
//            return photo;
//        } catch (Exception e) {
//            throw new TravelDiaryException("Failed to create test photo: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void writeTripsToFile() throws FileWriteException, IOException {
//        // Write trips to file
//        StorageWriter.writeTripsToFile(trips, new File(testFilePath), testFilePath);
//
//        // Read the file content
//        List<String> lines = Files.readAllLines(new File(testFilePath).toPath());
//
//        // Verify file content
//        assertTrue(lines.size() >= 6); // 2 trips, 2 albums, 2 photos
//
//        assertTrue(lines.get(0).startsWith("T | Test Trip 1 | Test Description 1"));
//        assertTrue(lines.get(1).startsWith("A | Test Trip 1"));
//        assertTrue(lines.get(2).startsWith("P | "));
//        assertTrue(lines.get(2).contains("Photo 1"));
//        assertTrue(lines.get(2).contains("Caption 1"));
//        assertTrue(lines.get(2).contains("2023-01-01"));
//        assertTrue(lines.get(2).contains("Location 1"));
//
//        assertTrue(lines.get(3).startsWith("T | Test Trip 2 | Test Description 2"));
//        assertTrue(lines.get(4).startsWith("A | Test Trip 2"));
//        assertTrue(lines.get(5).startsWith("P | "));
//        assertTrue(lines.get(5).contains("Photo 2"));
//        assertTrue(lines.get(5).contains("Caption 2"));
//    }
//
//    @Test
//    void writeTripsToFileWithNoAlbum() throws FileWriteException, IOException, TravelDiaryException {
//        // Create a trip with no album
//        Trip trip = new Trip("No Album Trip", "No Album Description");
//        trips.clear();
//        trips.add(trip);
//
//        // Write trips to file
//        StorageWriter.writeTripsToFile(trips, new File(testFilePath), testFilePath);
//
//        // Read the file content
//        List<String> lines = Files.readAllLines(new File(testFilePath).toPath());
//
//        // Verify file content
//        assertEquals(1, lines.size());
//        assertTrue(lines.get(0).startsWith("T | No Album Trip | No Album Description"));
//    }
//
//    @Test
//    void writeTripsToFileWithNoPhotos() throws FileWriteException, IOException, TravelDiaryException {
//        // Create a trip with empty album
//        Trip trip = new Trip("Empty Album Trip", "Empty Album Description");
//        trip.album = new Album();
//        trips.clear();
//        trips.add(trip);
//
//        // Write trips to file
//        StorageWriter.writeTripsToFile(trips, new File(testFilePath), testFilePath);
//
//        // Read the file content
//        List<String> lines = Files.readAllLines(new File(testFilePath).toPath());
//
//        // Verify file content
//        assertEquals(2, lines.size());
//        assertTrue(lines.get(0).startsWith("T | Empty Album Trip | Empty Album Description"));
//        assertTrue(lines.get(1).startsWith("A | Empty Album Trip"));
//    }
//
//    @Test
//    void writeTripsToFileWithSpecialCharacters() throws
//            FileWriteException, IOException, TravelDiaryException, ImageProcessingException {
//        // Create a trip with special characters
//        Trip trip = new Trip("Trip | With | Pipes", "Description\nWith\nNewlines");
//        trip.album = new Album();
//
//        Photo photo = createTestPhoto(createTestPhotoFile("photo.jpg"), "Photo | Name", "Caption\nWith\nNewlines",
//                LocalDateTime.of(2023, 1, 1, 12, 0));
//        trip.album.photos.add(photo);
//
//        trips.clear();
//        trips.add(trip);
//
//        // Write trips to file
//        StorageWriter.writeTripsToFile(trips, new File(testFilePath), testFilePath);
//
//        // Read the file content
//        List<String> lines = Files.readAllLines(new File(testFilePath).toPath());
//
//        // Verify file content
//        assertTrue(lines.get(0).contains("Trip \\| With \\| Pipes"));
//        assertTrue(lines.get(0).contains("Description\\nWith\\nNewlines"));
//        assertTrue(lines.get(2).contains("Photo \\| Name"));
//        assertTrue(lines.get(2).contains("Caption\\nWith\\nNewlines"));
//    }
//
//    private String createTestPhotoFile(String filename) {
//        try {
//            Path photoPath = tempDir.resolve(filename);
//            // Create a larger JPEG-like file with more headers
//            byte[] jpegData = new byte[1024]; // Larger file
//
//            // Standard JPEG header (SOI + APP0)
//            jpegData[0] = (byte) 0xFF;
//            jpegData[1] = (byte) 0xD8; // SOI marker
//            jpegData[2] = (byte) 0xFF;
//            jpegData[3] = (byte) 0xE0; // APP0 marker
//
//            // APP0 segment length (16 bytes)
//            jpegData[4] = 0x00;
//            jpegData[5] = 0x10;
//
//            // JFIF identifier
//            jpegData[6] = 0x4A; // J
//            jpegData[7] = 0x46; // F
//            jpegData[8] = 0x49; // I
//            jpegData[9] = 0x46; // F
//            jpegData[10] = 0x00;
//
//            // Add more fake segments
//            // Add an EOI marker at the end
//            jpegData[1022] = (byte) 0xFF;
//            jpegData[1023] = (byte) 0xD9;
//
//            Files.write(photoPath, jpegData);
//            return photoPath.toString();
//        } catch (IOException e) {
//            fail("Failed to create test photo file: " + e.getMessage());
//            return null;
//        }
//    }
//}
