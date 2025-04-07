package exception;

public class MissingTagsException extends ParserException{
    public static final String DEFAULT_MESSAGE =
            "\tMissing required tag(s) for " ;

    public MissingTagsException(String command,String tags) {
        super(ParserException.DEFAULT_MESSAGE + DEFAULT_MESSAGE + command + " Required tags: " + tags);
    }
}
