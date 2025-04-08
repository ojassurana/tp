package exception;

/**
 * Exception thrown when an operation requires a trip to be selected,
 * but no trip is currently selected in the application.
 */
public class TripNotSelectedException extends Exception {

    /**
     * The default error message displayed when this exception is thrown.
     */
    public static final String DEFAULT_MESSAGE =
            "\tYou must select a trip before adding a photo! " +
                    "Please select a trip first by entering 'select [trip index]'.";

    /**
     * Constructs a new {@code TripNotSelectedException} with the default error message.
     */
    public TripNotSelectedException() {
        super(DEFAULT_MESSAGE);
    }
}
