package exception;

public class DuplicateFilepathException extends Exception{
    public DuplicateFilepathException(String dataType, String name){
        super(dataType + " with filepath : '" + name + "' already exist in the list");
    }
}
