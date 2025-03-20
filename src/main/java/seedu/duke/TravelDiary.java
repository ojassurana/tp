package seedu.duke;

import command.Command;
import command.CommandFactory;
import exception.TravelDiaryException;
import parser.Parser;
import trip.TripManager;
import ui.Ui;

import java.util.Map;

public class TravelDiary {
    // FSM tracks which part of the code the user is in.
    // FSM Manual:
    // 0 -> User is yet to select a trip
    // 1 -> User is inside a trip right now
    public static int fsmValue = 0;
    public static void main(String[] args){
        Ui ui = new Ui();
        TripManager tripManager = new TripManager();
        ui.showWelcome();
        while (!processCommand(ui, tripManager)){
            ui.showLine();
        }
    }

    private static boolean processCommand(Ui ui, TripManager tripManager) {
        Map<String, String> parsedCommand;
        try {
            parsedCommand = Parser.getCommandDetails();
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
        } catch (TravelDiaryException e) {
            ui.showToUser(e.getMessage());
            return false;
        }
        return command.isExit();
    }
}
