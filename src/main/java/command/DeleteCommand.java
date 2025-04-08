package command;

import exception.IndexOutOfRangeException;
import trip.TripManager;
import ui.Ui;

/**
 * Represents a command to delete an item from the Travel Diary application.
 * The behavior changes based on the current FSM state:
 * - In Trip Page state (fsmValue = 0): Deletes a trip at the specified index
 * - In Photo Page state (fsmValue = 1): Deletes a photo at the specified index from the selected trip
 */
public class DeleteCommand extends Command {
    /** The index of the item to delete */
    private int index;

    /**
     * Constructs a DeleteCommand with the specified index.
     *
     * @param index the index of the item to delete (zero-based)
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the delete command, removing either a trip or a photo
     * depending on the current FSM state.
     *
     * @param tripManager the trip manager containing the trips and photos
     * @param ui the user interface (not directly used in this command)
     * @param fsmValue the current finite state machine value (0 for Trip Page, 1 for Photo Page)
     * @throws IndexOutOfRangeException if the specified index is out of range
     */
    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws IndexOutOfRangeException {
        if (fsmValue == 0) {
            tripManager.deleteTrip(index);
        } else if (fsmValue == 1) {
            tripManager.getSelectedTrip().album.deletePhoto(index);
        }
        this.fsmValue = fsmValue;
    }
}

