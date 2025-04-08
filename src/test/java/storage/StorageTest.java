package storage;

import com.drew.imaging.ImageProcessingException;
import exception.FileReadException;
import exception.FileFormatException;
import exception.FileWriteException;
import exception.NoMetaDataException;
import exception.TravelDiaryException;
import exception.DuplicateFilepathException;
import exception.DuplicateNameException;
import exception.MetadataFilepathNotFound;
import exception.MissingCompulsoryParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import trip.Trip;
import trip.TripManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for the Storage utility class.
 * This class contains unit tests for the storage functionality, including saving and loading trips,
 * handling various file conditions, and edge cases.
 */
class StorageTest {
    @TempDir
    Path tempDir;

    private TripManager tripManager;
    private String testFilePath;

    /**
     * Set up the test environment before each test.
     * Initializes a new TripManager and creates a temporary file path for testing.
     */
    @BeforeEach
    void setUp() {
        tripManager = new TripManager();
        testFilePath = tempDir.resolve("test_trips.dat").toString();
    }

    /**
     * Tests that the ensureFileExists method creates a new file when it doesn't exist.
     * This test uses reflection to access the private method.
     *
     * @throws IOException if there is an error creating the file
     */
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

    /**
     * Tests the full save and load cycle for trips.
     * Creates test trips, adds photos with metadata, saves them, and then loads them
     * into a new TripManager to verify the data integrity.
     *
     * @throws TravelDiaryException if there is an error in the travel diary operations
     * @throws FileWriteException if there is an error writing to the file
     * @throws FileReadException if there is an error reading from the file
     * @throws FileFormatException if the file format is invalid
     * @throws ImageProcessingException if there is an error processing the image
     * @throws IOException if there is an I/O error
     * @throws NoSuchAlgorithmException if a cryptographic algorithm is not available
     * @throws NoMetaDataException if metadata is missing
     * @throws DuplicateNameException if a duplicate name is detected
     * @throws MissingCompulsoryParameter if a required parameter is missing
     * @throws MetadataFilepathNotFound if the metadata filepath is not found
     * @throws DuplicateFilepathException if a duplicate filepath is detected
     */
    @Test
    void saveAndLoadTrips() throws TravelDiaryException, FileWriteException, FileReadException, FileFormatException,
            ImageProcessingException, IOException, NoSuchAlgorithmException, NoMetaDataException,
            DuplicateNameException, MissingCompulsoryParameter, MetadataFilepathNotFound, DuplicateFilepathException {
        // Create some test trips
        tripManager.addTrip("Test Trip 1", "Test Description 1");
        tripManager.addTrip("Test Trip 2", "Test Description 2");

        // Get the trips from the manager
        List<Trip> trips = tripManager.getTrips();
        Trip trip1 = trips.get(0);
        Trip trip2 = trips.get(1);

        // Add photos to trips using a real image with metadata
        LocalDateTime photoTime = LocalDateTime.now();

        trip1.album.addPhoto(copyRealTestPhoto("data/photos/samurai.jpg"), "Photo 1",
                "Caption 1", photoTime);
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

    /**
     * Tests that loading trips from a non-existent file creates an empty file.
     *
     * @throws FileReadException if there is an error reading from the file
     * @throws FileFormatException if the file format is invalid
     * @throws NoMetaDataException if metadata is missing
     */
    @Test
    void loadTripsWithNonExistentFileCreatesEmptyFile() throws FileReadException,
            FileFormatException, NoMetaDataException {
        String nonExistentPath = tempDir.resolve("non_existent.dat").toString();
        Storage.loadTrips(tripManager, nonExistentPath, true);

        // Verify file was created
        File file = new File(nonExistentPath);
        assertTrue(file.exists());

        // Verify no trips were loaded
        assertEquals(0, tripManager.getTrips().size());
    }

    /**
     * Tests that loading trips from a file with an invalid format throws a FileFormatException.
     *
     * @throws IOException if there is an I/O error
     */
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

    /**
     * Helper method to copy a real photo with EXIF metadata to the temporary directory.
     *
     * @param relativeFilePath the relative path to the source photo
     * @return the absolute path to the copied photo
     */
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

    /**
     * Tests that saving an empty trip list creates a file.
     *
     * @throws FileWriteException if there is an error writing to the file
     */
    @Test
    void saveEmptyTripListCreatesFile() throws FileWriteException {
        List<Trip> emptyTrips = List.of();
        Storage.saveTasks(emptyTrips, testFilePath);

        File file = new File(testFilePath);
        assertTrue(file.exists());
        assertEquals(0, file.length()); // File should be empty
    }

    /**
     * Tests that loading from an empty file does not throw an exception.
     *
     * @throws Exception if there is an unexpected error
     */
    @Test
    void loadFromEmptyFileDoesNotThrow() throws Exception {
        Files.createFile(Path.of(testFilePath)); // Create empty file

        Storage.loadTrips(tripManager, testFilePath, true);
        assertEquals(0, tripManager.getTrips().size());
    }

    /**
     * Tests that directories are created when missing when ensuring file existence.
     *
     * @throws IOException if there is an I/O error
     */
    @Test
    void ensureDirectoryCreatedWhenMissing() throws IOException {
        // Create a file path with a missing directory
        String filePathWithMissingDirectory = tempDir.resolve("missingDirectory/test_trips.dat").toString();
        File testFile = new File(filePathWithMissingDirectory);

        // Use reflection to access private method
        try {
            java.lang.reflect.Method method = Storage.class
                    .getDeclaredMethod("ensureFileExists", File.class, String.class);
            method.setAccessible(true);

            // Invoke the method to ensure the file is created
            boolean result = (boolean) method.invoke(null, testFile, filePathWithMissingDirectory);

            // Assert that the directory is created, and the file is created
            assertTrue(testFile.exists());
            assertTrue(testFile.getParentFile().exists());  // Check if the directory was created
        } catch (Exception e) {
            fail("Failed to invoke ensureFileExists method: " + e.getMessage());
        }
    }

    /**
     * Tests saving trips to a non-existent parent directory.
     *
     * @throws FileWriteException if there is an error writing to the file
     * @throws TravelDiaryException if there is an error in the travel diary operations
     * @throws DuplicateNameException if a duplicate name is detected
     * @throws MissingCompulsoryParameter if a required parameter is missing
     */
    @Test
    void saveTripsToNonExistentParentDirectory() throws FileWriteException, TravelDiaryException,
            DuplicateNameException, MissingCompulsoryParameter {
        // Create a file path with a non-existent parent directory
        String nonExistentParentPath = tempDir.resolve("nonexistentParentDir/test_trips.dat").toString();

        // Create some test trips
        tripManager.addTrip("Test Trip 1", "Test Description 1");
        tripManager.addTrip("Test Trip 2", "Test Description 2");

        // Save trips to the file
        Storage.saveTasks(tripManager.getTrips(), nonExistentParentPath);

        // Verify that the file was created
        File file = new File(nonExistentParentPath);
        assertTrue(file.exists());
        assertTrue(file.length() > 0); // The file should not be empty
    }
}
