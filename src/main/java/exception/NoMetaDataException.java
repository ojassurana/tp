package exception;

public class NoMetaDataException extends Exception {
    public static final String DEFAULT_MESSAGE =
            "\n\tThe photo uploaded has no metadata." ;
    public static final String GUIDE = "\n\tPlease upload an actual photo taken using a phone's camera.\n" +
            "\n\tStep by Step Guide:\n" +
            "\t1) Take a photo with your phone's camera.\n" +
            "\t2) Save the photo to your computer as a file (jpeg).\n" +
            "\t3) If using messengers like Telegram, please send the image as a FILE and not as a compressed image.\n";

    public NoMetaDataException(String message) {
        super(message);
    }

    public NoMetaDataException() {
        super(DEFAULT_MESSAGE + GUIDE);
    }
}
