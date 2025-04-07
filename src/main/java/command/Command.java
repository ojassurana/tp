package command;

import com.drew.imaging.ImageProcessingException;
import exception.*;

import trip.TripManager;
import ui.Ui;

import java.io.IOException;

/**
 * Represents an abstract command in the Travel Diary application.
 * All command implementations must extend this class and override the execute method.
 * Commands are created by the CommandFactory based on user input parsed by the Parser.
 */
public abstract class Command {
    /** 
     * The FSM (Finite State Machine) value representing the state of the application.
     * 0 = Main menu (Trip Page)
     * 1 = Inside a trip (Photo Page)
     */
    public int fsmValue;

    /**
     * Indicates whether this command will exit the application.
     * 
     * @return true if the command should terminate the application, false otherwise
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Executes the command with the given TripManager, UI, and FSM state value.
     * 
     * @param tripManager the trip manager that manages all trips
     * @param ui the user interface to display results
     * @param fsmValue the current finite state machine value (application state)
     * @throws TravelDiaryException if there is a general error during execution
     * @throws MissingCompulsoryParameter if a required parameter is missing
     * @throws InvalidIndexException if an invalid index is provided
     * @throws IOException if there is an error reading or writing files
     * @throws ImageProcessingException if there is an error processing an image
     * @throws NoMetaDataException if required metadata is missing from an image
     */
    public abstract void execute(TripManager tripManager, Ui ui, int fsmValue) throws
            TravelDiaryException, MissingCompulsoryParameter, InvalidIndexException, IOException,
            ImageProcessingException, NoMetaDataException, DuplicateNameException, MetadataFilepathNotFound, DuplicateFilepathException;
}
