package command;

import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

/**
 * Represents a command to exit the Travel Diary application.
 * When executed, this command displays a farewell message and signals
 * to the main application loop that it should terminate.
 */
public class ExitCommand extends Command {
    /**
     * Executes the exit command, displaying a farewell message to the user.
     *
     * @param tripManager the trip manager (not used in this command)
     * @param ui the user interface to display the farewell message
     * @param fsmValue the current finite state machine value (not used in this command)
     * @throws TravelDiaryException if there is an error during execution
     */
    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws TravelDiaryException {
        ui.showToUser("Alvida! Till we meet next time :)");
    }

    /**
     * Indicates that this command should cause the application to exit.
     *
     * @return true to signal that the application should exit
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
