package storage;

import exception.FileReadException;
import exception.FileFormatException;
import exception.FileWriteException;
import trip.Trip;
import trip.TripManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

public class Storage {
    protected static final Logger LOGGER = Logger.getLogger(Storage.class.getName());
    protected static final String TRIP_MARKER = "T";
    protected static final String PHOTO_MARKER = "P";
    protected static final String ALBUM_MARKER = "A";
    protected static final String DELIMITER = " | ";

    /**
     * Saves trips to a file
     */
    public static void saveTasks(List<Trip> trips, String filePath) throws FileWriteException {
        File dataFile = new File(filePath);
        // Create parent directory if it doesn't exist
        if (dataFile.getParentFile() != null) {
            dataFile.getParentFile().mkdirs();
        }

        StorageWriter.writeTripsToFile(trips, dataFile, filePath);
    }

    /**
     * Loads trips from a file
     *
     * @param tripManager The trip manager to add trips to
     * @param filePath    The path to the file containing trip data
     * @param silentMode  Whether to suppress console output during loading
     */
    public static void loadTrips(TripManager tripManager, String filePath, boolean silentMode)
            throws FileReadException, FileFormatException {
        LOGGER.info("Loading trips from file: " + filePath + " (silent mode: " + silentMode + ")");

        // Create the file if it does not exist
        File dataFile = new File(filePath);
        if (ensureFileExists(dataFile, filePath)) {
            return;  // Return early if we just created a new empty file
        }

        // Validate file format before processing
        validateFileFormat(dataFile, filePath);

        // Store current silent mode and set to requested mode
        boolean originalSilentMode = tripManager.isSilentMode();
        tripManager.setSilentMode(silentMode);

        try {
            StorageReader.readTripsFromFile(tripManager, dataFile, filePath);
        } finally {
            // Always restore the original silent mode, even if an exception occurs
            tripManager.setSilentMode(originalSilentMode);
            LOGGER.info("Restored original silent mode: " + originalSilentMode);
        }
    }

    /**
     * Loads trips from a file using default silent mode from TripManager
     */
    public static void loadTrips(TripManager tripManager, String filePath)
            throws FileReadException, FileFormatException {
        // Use the tripManager's current silent mode setting
        loadTrips(tripManager, filePath, tripManager.isSilentMode());
    }

    /**
     * Validates that the file contains proper formatted data
     *
     * @param dataFile The file to validate
     * @param filePath The path to the file (for logging)
     * @throws FileFormatException If the file format is invalid
     */
    private static void validateFileFormat(File dataFile, String filePath) throws FileFormatException {
        if (dataFile.length() == 0) {
            // Empty file is valid (no trips)
            return;
        }

        try {
            // Read the first line of the file to check format
            String firstLine = Files.lines(dataFile.toPath()).findFirst().orElse("");

            // Check if the file starts with one of our expected markers
            if (!firstLine.startsWith(TRIP_MARKER + DELIMITER) &&
                    !firstLine.startsWith(ALBUM_MARKER + DELIMITER) &&
                    !firstLine.startsWith(PHOTO_MARKER + DELIMITER)) {
                throw new FileFormatException(filePath, firstLine);
            }

            // Additional validation could be added here if needed

        } catch (IOException e) {
            // If we can't read the file, it's an issue at line 1
            throw new FileFormatException(filePath, 1, e);
        }
    }

    /**
     * Ensures that the file exists, creating it if necessary
     *
     * @param dataFile The file to check
     * @param filePath The path to the file (for logging)
     * @return true if a new empty file was created, false otherwise
     */
    private static boolean ensureFileExists(File dataFile, String filePath) {
        if (dataFile.exists()) {
            return false;
        }

        if (dataFile.getParentFile() != null) {
            dataFile.getParentFile().mkdirs();
        }

        try {
            boolean created = dataFile.createNewFile();
            if (created) {
                LOGGER.info("Created new file: " + filePath);
                return true;  // New empty file was created
            } else {
                LOGGER.warning("File already exists: " + filePath);
                return false;
            }
        } catch (java.io.IOException e) {
            LOGGER.warning("Could not create file: " + filePath + ". " + e.getMessage());
            return true;  // Return true to signal that we should return early
        }
    }
}
