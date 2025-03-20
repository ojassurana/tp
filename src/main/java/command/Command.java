package command;

import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

public abstract class Command {
    public int fsmValue;
    public abstract void execute(TripManager tripManager, Ui ui, int fsmValue) throws TravelDiaryException, MissingCompulsoryParameter;

    public boolean isExit() {
        return false;
    }
}
