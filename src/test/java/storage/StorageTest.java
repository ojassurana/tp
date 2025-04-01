package storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trip.Trip;
import trip.TripManager;
import exception.FileReadException;
import exception.FileFormatException;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTest {

    private TripManager tripManager;

    @BeforeEach
    void setUp() {
        tripManager = new TripManager();
    }

    @Test
    void testLoadNonExistentFile() throws FileReadException, FileFormatException {
        File nonExistentFile = new File("nonexistent.txt");

        List<Trip> trips = null;
        try {
            trips = Storage.loadTrips(tripManager, nonExistentFile.getAbsolutePath());
        } catch (FileReadException | FileFormatException e) {
            assertNotNull(e, "Exception should be thrown for non-existent file.");
        }

        assertNotNull(trips);
        assertTrue(trips.isEmpty(), "Trips list should be empty for a non-existent file");
    }
}
