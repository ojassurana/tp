package exception;

/**
 * Exception thrown when an error occurs while loading a photo from storage.
 */
public class PhotoLoadException extends StorageException {
    public PhotoLoadException(String photoName, String filePath) {
        super("Failed to load photo: '" + photoName
                + "' from file: " + filePath + ". The file may be missing or unreadable.");
    }

    public PhotoLoadException(String photoName, String filePath, Throwable cause) {
        super("Failed to load photo: '" + photoName
                + "' from file: " + filePath + ". Verify file existence and permissions.", cause);
    }
}
