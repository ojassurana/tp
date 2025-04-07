package command;

import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

/**
 * Represents a command to list items in the Travel Diary application.
 * The behavior changes based on the current FSM state:
 * - In Trip Page state (fsmValue = 0): Lists all trips
 * - In Photo Page state (fsmValue = 1): Lists all photos in the selected trip
 */
public class ListCommand extends Command {

    /**
     * Executes the list command, displaying either all trips or all photos
     * depending on the current FSM state.
     *
     * @param tripManager the trip manager containing the trips and photos to list
     * @param ui the user interface (not directly used in this command)
     * @param fsmValue the current finite state machine value (0 for Trip Page, 1 for Photo Page)
     * @throws TravelDiaryException if there is an error during execution
     */
    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws TravelDiaryException {
        if (fsmValue == 0){
            tripManager.viewTrips();
        } else if (fsmValue == 1) {
            tripManager.getSelectedTrip().album.viewPhotos();
        }
        this.fsmValue = fsmValue;
    }
}
