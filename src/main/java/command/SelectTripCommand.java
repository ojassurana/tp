package command;

import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

public class SelectTripCommand extends Command {
    private int index;

    public SelectTripCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TripManager tripManager, Ui ui) throws TravelDiaryException {
        tripManager.selectTrip(this.index);
    }
}
