package command;

import trip.TripManager;
import ui.Ui;

public class SelectCommand extends Command {
    @Override
    public void execute(TripManager tripManager, Ui ui) {
        ui.showToUser("Alvida! Till we meet next time :)");
    }

    @Override
    public boolean isExit() {
        return true;
    }
}