package exception;

/**
 * Exception thrown when an error occurs while writing to a file.
 */
public class FileWriteException extends StorageException {
    public FileWriteException(String filePath) {
        super("Failed to write to file: " + filePath + ". The file may be locked, read-only, or the disk is full.");
    }

    public FileWriteException(String filePath, Throwable cause) {
        super("Failed to write to file: " + filePath + ". Check disk space and file permissions.", cause);
    }
}
