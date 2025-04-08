package command;

import com.drew.imaging.ImageProcessingException;
import exception.MissingCompulsoryParameter;
import exception.NoMetaDataException;
import exception.TravelDiaryException;
import exception.DuplicateNameException;
import exception.DuplicateFilepathException;
import exception.MetadataFilepathNotFound;
import trip.TripManager;
import ui.Ui;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Represents a command to add a new photo to a trip in the Travel Diary application.
 * This command can only be executed when a trip is selected (FSM state 1).
 */
public class AddPhotoCommand extends Command {
    private static final Logger logger = Logger.getLogger(AddTripCommand.class.getName());
    /** The file path of the photo to add */
    private String filepath;
    /** The name to assign to the photo */
    private String photoname;
    /** The caption to assign to the photo */
    private String caption;

    /**
     * Constructs an AddPhotoCommand with the specified photo details.
     *
     * @param filepath the file path of the photo to add
     * @param photoname the name to assign to the photo
     * @param caption the caption to assign to the photo
     */
    public AddPhotoCommand(String filepath, String photoname, String caption) {
        logger.info("Creating AddPhotoCommand instance");
        this.filepath = filepath;
        this.photoname = photoname;
        this.caption = caption;
        // Assertions to ensure non-null parameters
        assert this.filepath != null : "Photoname should not be null";
        assert this.photoname != null : "Photo description should not be null";
        assert this.caption != null : "Photo caption should not be null";
    }

    /**
     * Executes the command to add a new photo to the currently selected trip.
     * The photo is added to the album of the selected trip.
     *
     * @param tripManager the trip manager that contains the selected trip
     * @param ui the user interface to display results
     * @param fsmValue the current finite state machine value (should be 1)
     * @throws TravelDiaryException if the tripManager or selected trip is null
     * @throws ImageProcessingException if there is an error processing the image
     * @throws NoMetaDataException if required metadata cannot be extracted from the photo
     * @throws MetadataFilepathNotFound metadata filepath can not be found
     * @throws DuplicateNameException exception due to same photo name in album
     * @throws DuplicateFilepathException exception due to same photo filepath in album
     */

    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws
            TravelDiaryException, ImageProcessingException,
            NoMetaDataException, MetadataFilepathNotFound, DuplicateNameException, DuplicateFilepathException {
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

        tripManager.getSelectedTrip().album.addPhoto(this.filepath, this.photoname, this.caption);
        this.fsmValue =fsmValue;
    }
}
