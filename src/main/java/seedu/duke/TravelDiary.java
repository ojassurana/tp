package seedu.duke;

import command.Command;
import command.CommandFactory;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import parser.Parser;
import photo.PhotoPrinter;
import storage.Storage;
import trip.TripManager;
import ui.Ui;
import exception.InvalidIndexException;
import exception.CommandNotRecogniseException;
import exception.TripNotSelectedException;

import java.util.List;
import java.util.Map;

public class TravelDiary {
    // Finite State Machine (FSM) to track user's current context
    // FSM States:
    // 0 -> No trip selected
    // 1 -> Inside a specific trip
    public static int fsmValue = 0;

    public static void main(String[] args) {
        Ui ui = new Ui();
        TripManager tripManager = new TripManager();

        // Load existing trips from storage
        List savedTrips = Storage.loadTrips(tripManager);

        ui.showWelcome();
        boolean exitProgram = false;
        while (!exitProgram) {
            try {
                exitProgram = processCommand(ui, tripManager);
            } catch (InvalidIndexException e) {
                ui.showToUser("Invalid index: " + e.getMessage());
            }
            ui.showLine();
        }

        // Save trips before exiting
        Storage.saveTasks(tripManager.getTrips());
        PhotoPrinter.closeAllWindows();
    }

    private static boolean processCommand(Ui ui, TripManager tripManager) throws InvalidIndexException {
        Map parsedCommand;
        try {
            parsedCommand = Parser.getCommandDetails();
        } catch (TravelDiaryException | CommandNotRecogniseException e) {
            ui.showToUser(e.getMessage());
            return false;
        }

        Command command;
        try {
            command = CommandFactory.getCommand(parsedCommand, fsmValue);
            command.execute(tripManager, ui, fsmValue);
            fsmValue = command.fsmValue;

            // Save trips after each command to maintain persistent storage
            Storage.saveTasks(tripManager.getTrips());
        } catch (TravelDiaryException |
                 NumberFormatException |
                 MissingCompulsoryParameter |
                 TripNotSelectedException e) {
            ui.showToUser(e.getMessage());
            return false;
        }

        return command.isExit();
    }
}
