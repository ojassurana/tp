package exception;

public class CommandNotRecogniseException extends Exception{
    public CommandNotRecogniseException(String command){
        super(String.format("\tCommand '%s' is not recognised.", command));
    }
}
