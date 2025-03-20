package command;

import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

public class AddTripCommand extends Command{
    private String name;
    private String description;
    private String location;

    public AddTripCommand(String name, String description, String location) {
        this.name = name;
        this.description = description;
        this.location = location;

    }

    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws TravelDiaryException, MissingCompulsoryParameter {
        tripManager.addTrip(this.name, this.description, this.location);
        this.fsmValue = fsmValue;
    }
}
