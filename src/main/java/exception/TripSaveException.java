package exception;

/**
 * Exception thrown when an error occurs while saving a trip to storage.
 */
public class TripSaveException extends StorageException {
    public TripSaveException(String tripName) {
        super("Failed to save trip: '" + tripName + "'. Ensure storage permissions and file accessibility.");
    }

    public TripSaveException(String tripName, Throwable cause) {
        super("Failed to save trip: '" + tripName + "'. Check disk space and file system issues.", cause);
    }
}
