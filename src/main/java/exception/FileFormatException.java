package exception;

/**
 * Exception thrown when a file's format is incorrect or corrupted.
 */
public class FileFormatException extends StorageException {
    public FileFormatException(String filePath, String line) {
        super("Invalid file format in: " + filePath + ". Error parsing line: \"" + line + "\". Data may be corrupted.");
    }

    public FileFormatException(String filePath, int lineNumber, Throwable cause) {
        super("Invalid file format in: " + filePath
                + " at line " + lineNumber + ". The format is incorrect or unexpected.", cause);
    }
}
