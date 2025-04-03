package storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import trip.TripManager;
import exception.FileReadException;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTest {

    private TripManager tripManager;

    @BeforeEach
    void setUp() {
        tripManager = new TripManager();
    }

    @Test
    void testLoadNonExistentFile() {
        File nonExistentFile = new File("nonexistent.txt");

        // The method is expected to throw a FileReadException
        FileReadException exception = assertThrows(
                FileReadException.class,
                () -> Storage.loadTrips(tripManager, nonExistentFile.getAbsolutePath())
        );

        // Verify the exception contains appropriate information
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("nonexistent.txt"));
    }

}
