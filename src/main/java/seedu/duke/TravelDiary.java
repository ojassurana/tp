package seedu.duke;

import command.Command;
import command.CommandFactory;
import exception.CommandNotRecogniseException;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import exception.WrongMachineState;
import parser.Parser;
import trip.TripManager;
import ui.Ui;

public class TravelDiary {
    // FSM means finite state machine. It tracks which part of the code the user is in
    // FSM Manual:
    // 0 -> User is yet to select a trip
    // 1 -> User is inside a trip right now
    public static int fsmValue = 0;
    public static void main(String[] args){
        // int fsm_value = 0;
        Ui ui = new Ui();
        TripManager tripManager = new TripManager();
        ui.showWelcome();
        while (!processCommand(ui, tripManager)){
            ui.showLine();
        }
    }

    private static boolean processCommand(Ui ui, TripManager tripManager) {
        String input = ui.readInput().trim();
        Parser parser;
        try {
            parser = new Parser(input);
        } catch (TravelDiaryException | CommandNotRecogniseException e) {
            ui.showToUser("exception encountered");
            return false;
        }
        ui.showLine();
        try {
            CommandFactory commandFactory = new CommandFactory();
            Command command = commandFactory.getCommand(tripManager, parser, fsmValue);
            command.execute(tripManager,ui); // need to add storage in these param in the future
            fsmValue = parser.fsmValue;
        } catch (TravelDiaryException | MissingCompulsoryParameter | WrongMachineState e) {
            ui.showToUser("\nexception encountered");
            return false;
        } catch (NumberFormatException e){
            ui.showToUser("number format exception encountered");
            ui.showToUser("please enter number for your index");
            ui.showToUser("eg. "+ parser.getHashmap().getOrDefault("command", "select ") +" 1");
            ui.showToUser("exception encountered");
            return false;
        }

        return parser.isExit;
    }
}
