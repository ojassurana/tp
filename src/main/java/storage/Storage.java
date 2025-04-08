package storage;

import exception.FileReadException;
import exception.FileFormatException;
import exception.FileWriteException;
import exception.NoMetaDataException;
import trip.Trip;
import trip.TripManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handles saving and loading trip data to and from the local file system.
 * Provides methods for ensuring data persistence and validating data formats.
 */
public class Storage {
    protected static final Logger LOGGER = Logger.getLogger(Storage.class.getName());
    protected static final String TRIP_MARKER = "T";
    protected static final String PHOTO_MARKER = "P";
    protected static final String ALBUM_MARKER = "A";
    protected static final String DELIMITER = " | ";

    /**
     * Saves a list of trips to a specified file path.
     *
     * @param trips    The list of {@code Trip} objects to be saved.
     * @param filePath The destination file path for saving trip data.
     * @throws FileWriteException If an error occurs while writing to the file.
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
     * Loads trips from the specified file and adds them to the given TripManager.
     *
     * @param tripManager The {@code TripManager} to which loaded trips will be added.
     * @param filePath    The path to the file containing stored trip data.
     * @param silentMode  Whether to suppress user-facing console messages during loading.
     * @throws FileReadException     If the file cannot be read.
     * @throws FileFormatException   If the file has an invalid or corrupted format.
     * @throws NoMetaDataException   If expected metadata is missing in the file.
     */
    public static void loadTrips(TripManager tripManager, String filePath, boolean silentMode)
            throws FileReadException, FileFormatException, NoMetaDataException {
        LOGGER.info("Loading trips from file: " + filePath + " (silent mode: " + silentMode + ")");

        File dataFile = new File(filePath);

        // Create new empty file if it doesn't exist
        if (ensureFileExists(dataFile, filePath)) {
            return;
        }

        // Validate file format before processing
        validateFileFormat(dataFile, filePath);

        // Store current silent mode and set to requested mode
        boolean originalSilentMode = tripManager.isSilentMode();
        tripManager.setSilentMode(silentMode);

        try {
            StorageReader.readTripsFromFile(tripManager, dataFile, filePath);
        } finally {
            // Always restore original silent mode
            tripManager.setSilentMode(originalSilentMode);
            LOGGER.info("Restored original silent mode: " + originalSilentMode);
        }
    }

    /**
     * Overloaded version of {@code loadTrips} that uses the TripManager's current silent mode setting.
     *
     * @param tripManager The {@code TripManager} to which loaded trips will be added.
     * @param filePath    The path to the file containing stored trip data.
     * @throws FileReadException     If the file cannot be read.
     * @throws FileFormatException   If the file has an invalid or corrupted format.
     * @throws NoMetaDataException   If expected metadata is missing in the file.
     */
    public static void loadTrips(TripManager tripManager, String filePath)
            throws FileReadException, FileFormatException, NoMetaDataException {
        loadTrips(tripManager, filePath, tripManager.isSilentMode());
    }

    /**
     * Validates whether the specified file is correctly formatted based on expected markers.
     * An empty file is considered valid.
     *
     * @param dataFile The file to validate.
     * @param filePath The file path (used for error messages).
     * @throws FileFormatException If the first line of the file has an unexpected format or can't be read.
     */
    private static void validateFileFormat(File dataFile, String filePath) throws FileFormatException {
        if (dataFile.length() == 0) {
            return;  // Empty file is valid
        }

        try {
            String firstLine = Files.lines(dataFile.toPath()).findFirst().orElse("");

            if (!firstLine.startsWith(TRIP_MARKER + DELIMITER) &&
                    !firstLine.startsWith(ALBUM_MARKER + DELIMITER) &&
                    !firstLine.startsWith(PHOTO_MARKER + DELIMITER)) {
                throw new FileFormatException(filePath, firstLine);
            }
        } catch (IOException e) {
            throw new FileFormatException(filePath, 1, e);
        }
    }

    /**
     * Ensures the specified file exists. If it does not exist, attempts to create it.
     *
     * @param dataFile The file to check or create.
     * @param filePath The file path (used for logging).
     * @return {@code true} if a new file was created; {@code false} if the file already existed.
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
                return true;
            } else {
                LOGGER.warning("File already exists: " + filePath);
                return false;
            }
        } catch (IOException e) {
            LOGGER.warning("Could not create file: " + filePath + ". " + e.getMessage());
            return true;
        }
    }
}
