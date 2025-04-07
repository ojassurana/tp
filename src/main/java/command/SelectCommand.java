package command;

import exception.InvalidIndexException;
import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

/**
 * Represents a command to select an item in the Travel Diary application.
 * The behavior changes based on the current FSM state:
 * - In Trip Page state (fsmValue = 0): Selects a trip at the specified index and changes to Photo Page state
 * - In Photo Page state (fsmValue = 1): Selects a photo at the specified index within the current trip
 */
public class SelectCommand extends Command {
    /** The index of the item to select */
    private int index;

    /**
     * Constructs a SelectCommand with the specified index.
     *
     * @param index the index of the item to select (zero-based)
     */
    public SelectCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the select command, selecting either a trip or a photo
     * depending on the current FSM state.
     *
     * @param tripManager the trip manager containing the trips and photos
     * @param ui the user interface (not directly used in this command)
     * @param fsmValue the current finite state machine value (0 for Trip Page, 1 for Photo Page)
     * @throws InvalidIndexException if the specified index is invalid
     * @throws TravelDiaryException if there is an error during execution
     */
    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue)
            throws InvalidIndexException, TravelDiaryException {
        if (fsmValue == 0) {
            tripManager.selectTrip(index);
            this.fsmValue = 1;
        } else if (fsmValue == 1) {
            tripManager.getSelectedTrip().album.selectPhoto(index);
            this.fsmValue = fsmValue;
        }
    }
}
