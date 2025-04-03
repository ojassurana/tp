package command;

import com.drew.imaging.ImageProcessingException;
import exception.InvalidIndexException;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

import java.io.IOException;

public abstract class Command {
    public int fsmValue;

    public boolean isExit() {
        return false;
    }

    public abstract void execute(TripManager tripManager, Ui ui, int fsmValue) throws
            TravelDiaryException, MissingCompulsoryParameter, InvalidIndexException, IOException, ImageProcessingException;
}
