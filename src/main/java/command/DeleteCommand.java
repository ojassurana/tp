package command;

import exception.IndexOutOfRangeException;
import trip.TripManager;
import ui.Ui;

public class DeleteCommand extends Command {
    private int index;
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) {
        if (fsmValue == 0){
            try {
                tripManager.deleteTrip(index);
            } catch (IndexOutOfRangeException e) {
                System.out.println(e.getMessage());
            }
        } else if (fsmValue == 1) {
            tripManager.getSelectedTrip().album.deletePhoto(index);
        }
        this.fsmValue =fsmValue;
    }
}
