package seedu.duke;

import com.drew.imaging.ImageProcessingException;
import command.Command;
import command.CommandFactory;
import exception.InvalidIndexException;
import exception.CommandNotRecogniseException;
import exception.TripNotSelectedException;
import exception.ParserException;
import exception.FileFormatException;
import exception.FileReadException;
import exception.FileWriteException;
import exception.TravelDiaryException;
import exception.MissingCompulsoryParameter;
import parser.Parser;
import photo.PhotoPrinter;
import storage.Storage;
import trip.TripManager;
import ui.Ui;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;


public class TravelDiary {
    public static int fsmValue = 0;
    private static final Logger logger = Logger.getLogger(TravelDiary.class.getName());
    private static final String FILE_PATH = "./data/travel_diary.txt";
    // Finite State Machine (FSM) to track user's current context
    // FSM States:
    // 0 -> No trip selected
    // 1 -> Inside a specific trip

    public static void main(String[] args) {
        Ui ui = new Ui();
        TripManager tripManager = new TripManager();
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.OFF);

        // Load existing trips from storage
        try {
            Storage.loadTrips(tripManager, FILE_PATH);
            ui.showWelcome();  // Show welcome message only after successful load
        } catch (FileReadException | FileFormatException e) {
            ui.showToUser("Error loading saved trips: " + e.getMessage());
            logger.log(Level.SEVERE, "Failed to load trips", e);
            return;  // Exit if there's an error loading trips
        }


        boolean exitProgram = false;
        while (!exitProgram) {
            ui.showFsmState(fsmValue);
            try {
                exitProgram = processCommand(ui, tripManager);
            } catch (InvalidIndexException e) {
                ui.showToUser("Invalid index: " + e.getMessage());
            }
            ui.showLine();
        }
        // Save trips before exiting
        try {
            Storage.saveTasks(tripManager.getTrips(), FILE_PATH);
        } catch (FileWriteException e) {
            ui.showToUser("Error saving trips: " + e.getMessage());
            logger.log(Level.SEVERE, "Failed to save trips", e);
        }
        PhotoPrinter.closeAllWindows();
    }

    private static boolean processCommand(Ui ui, TripManager tripManager) throws InvalidIndexException {
        Map<String, String> parsedCommand;
        try {
            parsedCommand = Parser.getCommandDetails();
        } catch (TravelDiaryException | CommandNotRecogniseException | ParserException e) {
            ui.showToUser(e.getMessage());
            return false;
        }

        Command command;
        try {
            command = CommandFactory.getCommand(parsedCommand, fsmValue);
            command.execute(tripManager, ui, fsmValue);
            fsmValue = command.fsmValue;

            // Save trips after each command to maintain persistent storage
            try {
                Storage.saveTasks(tripManager.getTrips(), FILE_PATH);
            } catch (FileWriteException e) {
                ui.showToUser("Error saving trips: " + e.getMessage());
                logger.log(Level.WARNING, "Failed to save trips after command", e);
            }
        } catch (TravelDiaryException |
                 NumberFormatException |
                 MissingCompulsoryParameter |
                 ImageProcessingException |
                 IOException |
                 TripNotSelectedException e) {
            ui.showToUser(e.getMessage());
            return false;
        }

        return command.isExit();
    }
}
