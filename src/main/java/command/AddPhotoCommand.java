package command;

import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;
import java.util.logging.Logger;

public class AddPhotoCommand extends Command {
    private static final Logger logger = Logger.getLogger(AddTripCommand.class.getName());
    private String filepath;
    private String photoname;
    private String caption;
    private String location;

    public AddPhotoCommand(String filepath, String photoname, String caption, String location) {
        logger.info("Creating AddPhotoCommand instance");
        this.filepath = filepath;
        this.photoname = photoname;
        this.caption = caption;
        this.location = location;
        // Assertions to ensure non-null parameters
        assert this.filepath != null : "Photoname should not be null";
        assert this.photoname != null : "Photo description should not be null";
        assert this.caption != null : "Photo caption should not be null";
        assert this.location != null : "Photo location should not be null";
    }

    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws
            TravelDiaryException, MissingCompulsoryParameter {
        logger.info("Executing AddPhotoCommand");

        if (tripManager == null) {
            logger.severe("TripManager is null");
            throw new TravelDiaryException("TripManager cannot be null");
        }
        if (tripManager.getSelectedTrip() == null) {
            logger.severe("Selected trip is null");
            throw new TravelDiaryException("selected trip cannot be null");
        }

        if (ui == null) {
            logger.warning("UI instance is null");
        }

        tripManager.getSelectedTrip().album.addPhoto(this.filepath, this.photoname, this.caption, this.location);
        this.fsmValue =fsmValue;
    }


}
