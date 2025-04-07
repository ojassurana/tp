package exception;

public class NoDateTimeMetaDataException extends NoMetaDataException {
    public static final String DEFAULT_MESSAGE =
            "\n\tThe photo uploaded has no DateTime metadata." ;

    public NoDateTimeMetaDataException() {
        super(DEFAULT_MESSAGE + GUIDE);
    }
}
