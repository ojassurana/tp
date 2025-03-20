package command;

import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

public class SelectTripCommand extends Command {
    private int index;

    public SelectTripCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws TravelDiaryException {
        ui.showToUser("Alvida! Till we meet next time :)");
    }
}
