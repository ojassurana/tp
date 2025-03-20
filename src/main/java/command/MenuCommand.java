package command;

import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

public class MenuCommand extends Command {
    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws TravelDiaryException {
        ui.showToUser("Alvida! Till we meet next time :)");
    }

    @Override
    public boolean isExit() {
        return true;
    }
}