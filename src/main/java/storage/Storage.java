package storage;

import exception.FileReadException;
import exception.FileFormatException;
import exception.FileWriteException;
import trip.Trip;
import trip.TripManager;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class Storage {
    protected static final Logger logger = Logger.getLogger(Storage.class.getName());
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
        logger.info("Loading trips from file: " + filePath + " (silent mode: " + silentMode + ")");

        // Create the file if it does not exist
        File dataFile = new File(filePath);
        if (!dataFile.exists()) {
            try {
                // Create parent directories if they don't exist
                if (dataFile.getParentFile() != null) {
                    dataFile.getParentFile().mkdirs();
                }

                if (dataFile.createNewFile()) {
                    logger.info("Created new file: " + filePath);
                    // If we just created a new empty file, there's nothing to load
                    // So return early without trying to read the file
                    return;
                } else {
                    logger.warning("File already exists: " + filePath);
                }
            } catch (java.io.IOException e) {
                // Log the error but don't throw an exception
                logger.warning("Could not create file: " + filePath + ". " + e.getMessage());
                // Just return without loading anything
                return;
            }
        }

        // Store current silent mode and set to requested mode
        boolean originalSilentMode = tripManager.isSilentMode();
        tripManager.setSilentMode(silentMode);

        try {
            StorageReader.readTripsFromFile(tripManager, dataFile, filePath);
        } finally {
            // Always restore the original silent mode, even if an exception occurs
            tripManager.setSilentMode(originalSilentMode);
            logger.info("Restored original silent mode: " + originalSilentMode);
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
}
