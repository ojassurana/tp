package exception;

public class TripNotSelectedException extends Exception{
    public static final String DEFAULT_MESSAGE =
            "\tYou must select a trip before adding a photo! " +
                    "Please select a trip first by entering 'select [trip index]'.";

    public TripNotSelectedException() {
        super(DEFAULT_MESSAGE);
    }

}
