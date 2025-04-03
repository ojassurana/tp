package exception;

public class TagException extends ParserException{
    public TagException(String msg, String tag) {
        super(ParserException.DEFAULT_MESSAGE + msg + tag);
    }
}
