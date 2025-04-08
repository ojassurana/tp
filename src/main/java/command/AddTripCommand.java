package command;

import exception.DuplicateNameException;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;
import java.util.logging.Logger;

/**
 * Represents a command to add a new trip to the Travel Diary.
 * This command requires a name and description for the trip.
 */
public class AddTripCommand extends Command {
    private static final Logger logger = Logger.getLogger(AddTripCommand.class.getName());

    private String name;
    private String description;
    private String location;

    /**
     * Constructs an AddTripCommand with the specified trip details.
     *
     * @param name the name of the trip
     * @param description the description of the trip
     * @param location the location of the trip (optional)
     */
    public AddTripCommand(String name, String description, String location) {
        logger.info("Creating AddTripCommand instance");

        this.name = name;
        this.description = description;
        this.location = location;

        // Assertions to ensure non-null parameters
        assert this.name != null : "Trip name should not be null";
        assert this.description != null : "Trip description should not be null";
        assert this.location != null : "Trip location should not be null";
    }

    /**
     * Executes the command to add a new trip with the specified details.
     * The trip is added to the TripManager and becomes available in the application.
     *
     * @param tripManager the trip manager to add the trip to
     * @param ui the user interface to display results
     * @param fsmValue the current finite state machine value (application state)
     * @throws TravelDiaryException if the tripManager is null or another error occurs
     * @throws MissingCompulsoryParameter if required parameters are missing
     */
    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws
            TravelDiaryException, MissingCompulsoryParameter, DuplicateNameException {
        logger.info("Executing AddTripCommand");

        if (tripManager == null) {
            logger.severe("TripManager is null");
            throw new TravelDiaryException("TripManager cannot be null");
        }
        if (ui == null) {
            logger.warning("UI instance is null");
        }
        tripManager.addTrip(this.name, this.description);
        this.fsmValue = fsmValue;

        logger.info("Trip successfully added: " + this.name);
    }
}
