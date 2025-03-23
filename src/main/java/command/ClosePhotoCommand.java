package command;

import photo.PhotoPrinter;
import trip.TripManager;
import ui.Ui;

public class ClosePhotoCommand extends Command {
    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue)  {
        PhotoPrinter.closeAllWindows();
    }
}
