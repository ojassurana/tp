package command;

import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

/**
 * Represents a command to return to the main menu (Trip Page) in the Travel Diary application.
 * This command changes the FSM state back to 0 (Trip Page), regardless of the current state.
 */
public class MenuCommand extends Command {
    /**
     * Executes the menu command, returning to the Trip Page by setting fsmValue to 0.
     *
     * @param tripManager the trip manager (not directly used in this command)
     * @param ui the user interface (not directly used in this command)
     * @param fsmValue the current finite state machine value (ignored, as we always return to state 0)
     * @throws TravelDiaryException if there is an error during execution
     */
    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws TravelDiaryException {
        this.fsmValue = 0;
    }
}
