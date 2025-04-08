package exception;

public class MetadataFilepathNotFound extends Exception{
    public MetadataFilepathNotFound(String filepath) {
        super("file : " + filepath + " not found");
    }
}
