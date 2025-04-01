package exception;

/**
 * Exception thrown when an error occurs while loading a trip from storage.
 */
public class TripLoadException extends StorageException {
    public TripLoadException(String tripName) {
        super("Failed to load trip: '" + tripName + "'. The trip data may be missing or corrupted.");
    }

    public TripLoadException(String tripName, Throwable cause) {
        super("Failed to load trip: '" + tripName + "'. Ensure data integrity and correct format.", cause);
    }
}
