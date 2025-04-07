package command;

import photo.PhotoPrinter;
import trip.TripManager;
import ui.Ui;

/**
 * Represents a command to close all photo windows in the Travel Diary application.
 * This command uses the PhotoPrinter to close any open photo display windows.
 */
public class ClosePhotoCommand extends Command {
    /**
     * Executes the close photo command, closing all open photo windows.
     *
     * @param tripManager the trip manager (not used in this command)
     * @param ui the user interface (not used in this command)
     * @param fsmValue the current finite state machine value (not used in this command)
     */
    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue)  {
        PhotoPrinter.closeAllWindows();
    }
}
