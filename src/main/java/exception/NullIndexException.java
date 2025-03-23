package exception;

public class NullIndexException extends InvalidIndexException{
    public static final String DEFAULT_MESSAGE =
            "\tIndex was not specified." ;

    public NullIndexException() {
        super(DEFAULT_MESSAGE + InvalidIndexException.DEFAULT_MESSAGE);
    }

}
