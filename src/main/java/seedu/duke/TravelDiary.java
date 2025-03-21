package seedu.duke;

import command.Command;
import command.CommandFactory;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import parser.Parser;
import photo.PhotoPrinter;
import trip.TripManager;
import ui.Ui;

import java.util.Map;
import java.util.logging.Logger; // Added import for logging

public class TravelDiary {
    // FSM tracks which part of the code the user is in.
    // FSM Manual:
    // 0 -> User is yet to select a trip
    // 1 -> User is inside a trip right now
    public static int fsmValue = 0;

    // Initialize logger
    private static final Logger logger = Logger.getLogger(TravelDiary.class.getName());

    public static void main(String[] args) {
        Ui ui = new Ui();
        TripManager tripManager = new TripManager();
        ui.showWelcome();
        while (!processCommand(ui, tripManager)) {
            ui.showLine();
        }
        PhotoPrinter.closeAllWindows();
    }

    private static boolean processCommand(Ui ui, TripManager tripManager) {
        Map<String, String> parsedCommand;
        try {
            parsedCommand = Parser.getCommandDetails();
            // Log the parsed command for debugging purposes
            logger.info("Parsed command: " + parsedCommand.toString());
        } catch (TravelDiaryException e) {
            ui.showToUser(e.getMessage());
            return false;
        }
        ui.showLine();
        Command command;
        try {
            command = CommandFactory.getCommand(parsedCommand, fsmValue);
            command.execute(tripManager, ui, fsmValue);
            fsmValue = command.fsmValue;
        } catch (TravelDiaryException | NumberFormatException | MissingCompulsoryParameter e) {
            ui.showToUser(e.getMessage());
            return false;
        }
        return command.isExit();
    }
}
