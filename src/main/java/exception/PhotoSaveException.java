package exception;

/**
 * Exception thrown when an error occurs while saving a photo to storage.
 */
public class PhotoSaveException extends StorageException {
    public PhotoSaveException(String photoName, String filePath) {
        super("Failed to save photo: '" + photoName +
                "' to file: " + filePath + ". The file may be write-protected or the disk is full.");
    }

    public PhotoSaveException(String photoName, String filePath, Throwable cause) {
        super("Failed to save photo: '" + photoName +
                "' to file: " + filePath + ". Check file system permissions and storage availability.", cause);
    }
}
