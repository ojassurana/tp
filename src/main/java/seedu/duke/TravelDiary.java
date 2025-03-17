package seedu.duke;

import exception.TravelDiaryException;
import parser.Parser;
import trip.TripManager;
import ui.Ui;

public class TravelDiary {
    // FSM means finite state machine. It tracks which part of the code the user is in
    // FSM Manual:
    // 0 -> User is yet to select a trip
    // 1 -> User is inside a trip right now
    public static int fsmValue = 0;
    public static void main(String[] args) throws TravelDiaryException {
        // int fsm_value = 0;
        Ui ui = new Ui();
        TripManager tripManager = new TripManager();
        ui.showWelcome();
        while (!processCommand(ui, tripManager)){
            ui.showLine();
        }
    }

    private static boolean processCommand(Ui ui, TripManager tripManager) throws TravelDiaryException {
        String input = ui.readInput().trim();
        Parser parser;
        try {
            parser = new Parser(input);
        } catch (TravelDiaryException e) {
            ui.showToUser(e.getMessage());
            return false;
        }
        ui.showLine();
        try {
            parser.execute(tripManager, fsmValue);
            fsmValue = parser.fsmValue;
        } catch (TravelDiaryException e) {
            ui.showToUser(e.getMessage());
            return false;
        }

        return parser.isExit;
    }
}
