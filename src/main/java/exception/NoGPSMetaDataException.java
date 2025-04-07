package exception;

public class NoGPSMetaDataException extends NoMetaDataException {
    public static final String DEFAULT_MESSAGE =
            "\n\tThe photo uploaded has no GPS metadata." ;

    public NoGPSMetaDataException() {
        super(DEFAULT_MESSAGE + GUIDE);
    }
}
