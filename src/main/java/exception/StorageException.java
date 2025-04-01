package exception;

import java.io.IOException;

/**
 * Base exception class for handling storage-related errors in Travel Diary.
 */
public class StorageException extends IOException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
