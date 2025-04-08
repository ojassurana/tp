package exception;

public class MissingCompulsoryParameter extends Exception {
    public MissingCompulsoryParameter(String dataType , String parameters) {
        super("missing compulsory parameters for " + dataType + ": " + parameters);
    }
}
