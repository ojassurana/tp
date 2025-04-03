package exception;

public class ParserException extends Exception{
    public static final String DEFAULT_MESSAGE =
            "Something went wrong in parsing\n" ;

    public ParserException() {
        super(DEFAULT_MESSAGE);
    }
    public ParserException(String message) {
        super(message);
    }
}
