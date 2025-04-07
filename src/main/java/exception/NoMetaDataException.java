package exception;

/**
 * Exception thrown when the uploaded photo has no metadata.
 * This exception indicates that the photo file does not contain sufficient metadata,
 * such as location, date/time, or other details typically expected in a photo file.
 */
public class NoMetaDataException extends Exception {
    public static final String DEFAULT_MESSAGE =
            "\n\tThe photo uploaded has no metadata.";
    public static final String GUIDE = "\n\tPlease upload an actual photo taken using a phone's camera.\n" +
            "\n\tStep by Step Guide:\n" +
            "\t1) Take a photo with your phone's camera.\n" +
            "\t2) Save the photo to your computer as a file (jpeg).\n" +
            "\t3) If using messengers like Telegram, please send the image as a FILE and not as a compressed image.\n";

    /**
     * Constructs a NoMetaDataException with a custom message.
     * @param message The custom message describing the exception.
     */
    public NoMetaDataException(String message) {
        super(message);
    }

    /**
     * Constructs a NoMetaDataException with the default message and guide.
     */
    public NoMetaDataException() {
        super(DEFAULT_MESSAGE + GUIDE);
    }
}
