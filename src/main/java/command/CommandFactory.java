package command;

import exception.TravelDiaryException;
import exception.TripNotSelectedException;

import java.util.Map;

/**
 * Factory class that creates Command objects based on parsed user input.
 * This class determines which command to create based on the command name
 * and the current finite state machine (FSM) value.
 */
public class CommandFactory {

    /**
     * Creates and returns a Command object based on the parsed command details and FSM state.
     * 
     * @param parsedCommand map containing the parsed command details
     * @param fsmValue the current FSM state (0 for Trip Page, 1 for Photo Page)
     * @return the appropriate Command object
     * @throws TravelDiaryException if there is an error creating the command
     * @throws NumberFormatException if a numeric parameter cannot be parsed
     * @throws TripNotSelectedException if a trip-specific command is used without selecting a trip
     */
    public static Command getCommand(Map<String, String> parsedCommand, int fsmValue) throws
            TravelDiaryException, NumberFormatException, TripNotSelectedException {
        String cmd = parsedCommand.get("command");

        // Handle commands available in both FSM states.
        if ("bye".equals(cmd)) {
            return new ExitCommand();
        }
        if ("close".equals(cmd)) {
            return new ClosePhotoCommand();
        }
        if ("select".equals(cmd)) {
            int index = Integer.parseInt(parsedCommand.get("index")) - 1;
            return new SelectCommand(index); // Insert index in, update FSM value
        }
        if ("list".equals(cmd)) {
            return new ListCommand();
        }
        if ("delete".equals(cmd)) {
            int index = Integer.parseInt(parsedCommand.get("index")) - 1;
            return new DeleteCommand(index); // Insert index in
        }

        if ("help".equals(cmd)) {
            // Help command is available in all states
            return new HelpCommand(parsedCommand.getOrDefault("fsm", null));
        }

        // Delegate state-specific commands.
        if (fsmValue == 0) {
            return handleMenuStateCommand(parsedCommand);
        } else if (fsmValue == 1) {
            return handleTripStateCommand(parsedCommand);
        } else {
            throw new TravelDiaryException("Invalid state.");
        }
    }

    /**
     * Handles commands specific to the Trip Page (main menu) state.
     * 
     * @param parsedCommand map containing the parsed command details
     * @return the appropriate Command object for the Trip Page state
     * @throws TravelDiaryException if there is an error creating the command
     * @throws TripNotSelectedException if a trip-specific command is used in this state
     */
    private static Command handleMenuStateCommand(Map<String, String> parsedCommand)
            throws TravelDiaryException, TripNotSelectedException {
        String cmd = parsedCommand.get("command");
        if ("add_trip".equals(cmd)) {
            String name = parsedCommand.get("name");
            String description = parsedCommand.get("description");
            String location = parsedCommand.get("location");
            return new AddTripCommand(name, description, location);
        }
        if ("menu".equals(cmd)) {
            throw new TravelDiaryException("You are already in the menu.");
        }
        throw new TripNotSelectedException();
    }

    /**
     * Handles commands specific to the Photo Page (trip selected) state.
     * 
     * @param parsedCommand map containing the parsed command details
     * @return the appropriate Command object for the Photo Page state
     * @throws TravelDiaryException if there is an error creating the command
     */
    private static Command handleTripStateCommand(Map<String, String> parsedCommand) throws TravelDiaryException {
        String cmd = parsedCommand.get("command");
        if ("add_photo".equals(cmd)) {
            String filepath = parsedCommand.get("filepath");
            String photoname = parsedCommand.get("photoname");
            String caption = parsedCommand.get("caption");
            return new AddPhotoCommand(filepath, photoname, caption);
        }
        if ("menu".equals(cmd)) {
            // Assuming MenuCommand resets FSM state and performs any required housekeeping.
            return new MenuCommand();
        }
        throw new TravelDiaryException("Please go back to main menu first before adding a trip. Use 'menu' command");
    }
}
