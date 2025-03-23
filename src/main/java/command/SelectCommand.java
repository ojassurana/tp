package command;

import exception.InvalidIndexException;
import trip.TripManager;
import ui.Ui;

public class SelectCommand extends Command {
    private int index;

    public SelectCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws InvalidIndexException {
        if (fsmValue == 0){
            try {
                tripManager.selectTrip(index);
            } catch (InvalidIndexException e) {
                System.out.println(e.getMessage());
            }
        } else if (fsmValue == 1) {
            tripManager.getSelectedTrip().album.selectPhoto(index);
        }
        this.fsmValue = 1;

    }
}
