package command;

import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

public class ListCommand extends Command {

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
