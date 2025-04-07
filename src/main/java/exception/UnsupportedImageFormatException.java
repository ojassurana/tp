package exception;

/**
 * Exception thrown when an unsupported image format is provided.
 * This program only accepts .jpg images.
 */
public class UnsupportedImageFormatException extends TravelDiaryException {
    public UnsupportedImageFormatException(String filePath) {
        super("Unsupported image format: " + filePath + ". This program only accepts .jpg images.");
    }
} 
