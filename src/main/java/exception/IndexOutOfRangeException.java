package exception;

public class IndexOutOfRangeException extends InvalidIndexException{
    public static final String DEFAULT_MESSAGE =
            "\tIndex is out of range!" ;

    public IndexOutOfRangeException() {
        super(DEFAULT_MESSAGE + InvalidIndexException.DEFAULT_MESSAGE);
    }

}
