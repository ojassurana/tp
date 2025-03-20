package command;

import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

public abstract class Command {
    public abstract void execute(TripManager tripManager, Ui ui) throws MissingCompulsoryParameter,
            TravelDiaryException;
}
