package exception;

public class DuplicateNameException extends Exception{
    public DuplicateNameException(String dataType, String name){
        super(dataType + " with name : '" + name + "' already exist in the list");
    }
}
