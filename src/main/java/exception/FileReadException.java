package exception;

/**
 * Exception thrown when an error occurs while reading a file.
 */
public class FileReadException extends StorageException {
    public FileReadException(String filePath) {
        super("Failed to read file: " + filePath + ". The file might be missing, corrupted, or inaccessible.");
    }

    public FileReadException(String filePath, Throwable cause) {
        super("Failed to read file: " + filePath + ". Check file permissions or format.", cause);
    }
}
