//package storage;
//
//import exception.FileFormatException;
//import exception.TravelDiaryException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.io.TempDir;
//import photo.Photo;
//import trip.Trip;
//import trip.TripManager;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.fail;
//
//class StorageReaderTest {
//    @TempDir
//    Path tempDir;
//
//    private TripManager tripManager;
//    private String testFilePath;
//
//    @BeforeEach
//    void setUp() {
//        tripManager = new TripManager();
//        testFilePath = tempDir.resolve("test_trips.dat").toString();
//    }
//
//    @Test
//    void readTripsFromFileWithValidFormat() throws IOException, TravelDiaryException, FileFormatException {
//        // Create a valid trip file
//        String fileContent =
//                "T | Test Trip | Test Description\n" +
//                        "A | Test Trip\n" +
//                        "P | " + createTestPhotoFile("test.jpg") + " | Photo Name | Caption | 2023-01-01 12:00:00\n";
//
//        Files.write(new File(testFilePath).toPath(), fileContent.getBytes());
//
//        // Read trips from file
//        StorageReader.readTripsFromFile(tripManager, new File(testFilePath), testFilePath);
//
//        // Verify trips were read correctly
//        assertEquals(1, tripManager.getTrips().size());
//        Trip trip = tripManager.getTrips().get(0);
//        assertEquals("Test Trip", trip.name);
//        assertEquals("Test Description", trip.description);
//        assertEquals(1, trip.album.photos.size());
//
//        Photo photo = trip.album.photos.get(0);
//        assertEquals("Photo Name", photo.getPhotoName());
//        assertEquals("Caption", photo.getCaption());
//        assertNotNull(photo.getDatetime());
//    }
//
//    @Test
//    void readTripsFromFileWithMissingFields() throws IOException {
//        // Create a file with missing fields
//        String fileContent = "T | \n";
//        Files.write(new File(testFilePath).toPath(), fileContent.getBytes());
//
//        // Attempt to read trips
//        assertThrows(FileFormatException.class, () ->
//                StorageReader.readTripsFromFile(tripManager, new File(testFilePath), testFilePath)
//        );
//    }
//
//    @Test
//    void readTripsFromFileWithEscapedDelimiters() throws IOException, FileFormatException {
//        // Create a file with escaped delimiters
//        String fileContent =
//                "T | Trip\\|With\\|Pipes | Description\\|With\\|Pipes\n" +
//                        "A | Trip\\|With\\|Pipes\n";
//
//        Files.write(new File(testFilePath).toPath(), fileContent.getBytes());
//
//        // Read trips from file
//        StorageReader.readTripsFromFile(tripManager, new File(testFilePath), testFilePath);
//
//        // Verify trips were read correctly
//        assertEquals(1, tripManager.getTrips().size());
//        Trip trip = tripManager.getTrips().get(0);
//        assertEquals("Trip|With|Pipes", trip.name);
//        assertEquals("Description|With|Pipes", trip.description);
//    }
//
//    @Test
//    void readTripsFromFileWithAlbumWithoutTrip() throws IOException {
//        // Create a file with album without trip
//        String fileContent = "A | Album Name\n";
//        Files.write(new File(testFilePath).toPath(), fileContent.getBytes());
//
//        // Attempt to read trips
//        assertThrows(FileFormatException.class, () ->
//                StorageReader.readTripsFromFile(tripManager, new File(testFilePath), testFilePath)
//        );
//    }
//
//    @Test
//    void readTripsFromFileWithPhotoWithoutAlbum() throws IOException {
//        // Create a file with photo without album
//        String fileContent =
//                "T | Test Trip | Test Description\n" +
//                        "P | " + createTestPhotoFile("test.jpg") + " | Photo Name | Caption\n";
//
//        Files.write(new File(testFilePath).toPath(), fileContent.getBytes());
//
//        // Attempt to read trips
//        assertThrows(FileFormatException.class, () ->
//                StorageReader.readTripsFromFile(tripManager, new File(testFilePath), testFilePath)
//        );
//    }
//
//    private String createTestPhotoFile(String filename) {
//        try {
//            Path photoPath = tempDir.resolve(filename);
//            Files.createFile(photoPath);
//            return photoPath.toString();
//        } catch (IOException e) {
//            fail("Failed to create test photo file: " + e.getMessage());
//            return null;
//        }
//    }
//}
