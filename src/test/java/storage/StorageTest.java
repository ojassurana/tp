package storage;

import com.drew.imaging.ImageProcessingException;
import exception.FileFormatException;
import exception.FileReadException;
import exception.FileWriteException;
import exception.TravelDiaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import trip.Trip;
import trip.TripManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class StorageTest {
    @TempDir
    Path tempDir;

    private TripManager tripManager;
    private String testFilePath;

    @BeforeEach
    void setUp() {
        tripManager = new TripManager();
        testFilePath = tempDir.resolve("test_trips.dat").toString();
    }

    @Test
    void ensureFileExistsCreatesNewFile() throws IOException {
        // Use reflection to access private method
        java.lang.reflect.Method method;
        try {
            method = Storage.class.getDeclaredMethod("ensureFileExists", File.class, String.class);
            method.setAccessible(true);

            File testFile = new File(testFilePath);
            boolean result = (boolean) method.invoke(null, testFile, testFilePath);

            assertTrue(result);
            assertTrue(testFile.exists());
        } catch (Exception e) {
            fail("Failed to invoke ensureFileExists method: " + e.getMessage());
        }
    }

    @Test
    void saveAndLoadTrips() throws TravelDiaryException, FileWriteException, FileReadException, FileFormatException,
            ImageProcessingException, IOException {
        // Create some test trips
        tripManager.addTrip("Test Trip 1", "Test Description 1");
        tripManager.addTrip("Test Trip 2", "Test Description 2");

        // Get the trips from the manager
        List<Trip> trips = tripManager.getTrips();
        Trip trip1 = trips.get(0);
        Trip trip2 = trips.get(1);

        // Add photos to trips using a real image with metadata
        LocalDateTime photoTime = LocalDateTime.now();

        trip1.album.addPhoto(copyRealTestPhoto("data/photos/samurai.jpg"), "Photo 1", "Caption 1", photoTime);
        trip2.album.addPhoto(copyRealTestPhoto("data/photos/clem_with_metadata.jpg"),
                "Photo 2", "Caption 2", photoTime);
        // Save trips
        Storage.saveTasks(trips, testFilePath);

        // Create a new trip manager and load trips
        TripManager newTripManager = new TripManager();
        Storage.loadTrips(newTripManager, testFilePath, true);

        // Verify trips were loaded correctly
        assertEquals(2, newTripManager.getTrips().size());

        Trip loadedTrip1 = newTripManager.getTrips().get(0);
        Trip loadedTrip2 = newTripManager.getTrips().get(1);

        assertEquals("Test Trip 1", loadedTrip1.name);
        assertEquals("Test Description 1", loadedTrip1.description);
        assertEquals(1, loadedTrip1.album.photos.size());

        assertEquals("Test Trip 2", loadedTrip2.name);
        assertEquals("Test Description 2", loadedTrip2.description);
        assertEquals(1, loadedTrip2.album.photos.size());
    }

    @Test
    void loadTripsWithNonExistentFileCreatesEmptyFile() throws FileReadException, FileFormatException {
        String nonExistentPath = tempDir.resolve("non_existent.dat").toString();
        Storage.loadTrips(tripManager, nonExistentPath, true);

        // Verify file was created
        File file = new File(nonExistentPath);
        assertTrue(file.exists());

        // Verify no trips were loaded
        assertEquals(0, tripManager.getTrips().size());
    }

    @Test
    void loadTripsWithInvalidFormatThrowsException() throws IOException {
        // Create a file with invalid format
        File invalidFile = new File(testFilePath);
        Files.write(invalidFile.toPath(), "Invalid format".getBytes());

        // Attempt to load trips
        assertThrows(FileFormatException.class, () ->
                Storage.loadTrips(tripManager, testFilePath, true)
        );
    }

    // Helper method to copy a real photo with EXIF metadata
    private String copyRealTestPhoto(String relativeFilePath) {
        try {
            // Get the project root directory
            File projectRoot = new File(System.getProperty("user.dir"));
            // Create path to the photo
            File sourceFile = new File(projectRoot, relativeFilePath);

            if (!sourceFile.exists()) {
                fail("Test photo doesn't exist at: " + sourceFile.getAbsolutePath());
            }

            String fileName = sourceFile.getName();
            Path destinationPath = tempDir.resolve(fileName);
            Files.copy(sourceFile.toPath(), destinationPath);
            return destinationPath.toString();
        } catch (Exception e) {
            fail("Failed to copy real test photo: " + e.getMessage());
            return null;
        }
    }
}
