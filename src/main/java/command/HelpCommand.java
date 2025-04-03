package command;

import exception.InvalidIndexException;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import trip.TripManager;
import ui.Ui;

/**
 * Represents a command to display help information.
 */
public class HelpCommand extends Command {
    private int helpFsmValue;

    /**
     * Creates a new HelpCommand.
     *
     * @param helpFsmValue The FSM value to use for help display. If null, uses current application FSM value.
     */
    public HelpCommand(String helpFsmValue) {
        if (helpFsmValue != null) {
            try {
                this.helpFsmValue = Integer.parseInt(helpFsmValue);
            } catch (NumberFormatException e) {
                this.helpFsmValue = -1; // Invalid FSM value, will use current app FSM value
            }
        } else {
            this.helpFsmValue = -1; // No FSM value provided, will use current app FSM value
        }
    }

    @Override
    public void execute(TripManager tripManager, Ui ui, int fsmValue) throws
            TravelDiaryException, MissingCompulsoryParameter, InvalidIndexException {

        // If a valid FSM value was provided in the command, use it, otherwise use the current FSM value
        int fsmToUse = (helpFsmValue == 0 || helpFsmValue == 1) ? helpFsmValue : fsmValue;

        showHelp(fsmToUse);
    }

    /**
     * Displays help information based on the current state.
     * FSM = 0: General commands and Trip commands
     * FSM = 1: General commands and Photo commands
     *
     * @param fsm The current state of the finite state machine
     */
    private void showHelp(int fsm) {
        System.out.println("=== Travel Diary Help ===");

        // General commands - always available
        System.out.println("\nGeneral Commands:");
        System.out.println("  help              - Display this help information");
        System.out.println("  bye               - Exit the application");
        System.out.println("  menu              - Return to main menu");

        // State-specific commands
        if (fsm == 0) {
            // Trip management commands
            System.out.println("\nTrip Commands:");
            System.out.println("  list                - List all trips");
            System.out.println("  add_trip n# d# l#   - Add a new trip");
            System.out.println("                         n# - Trip name");
            System.out.println("                         d# - Trip description");
            System.out.println("  select <index>      - Select a trip to view/edit");
            System.out.println("  delete <index>      - Delete a specific trip");
        } else if (fsm == 1) {
            // Photo management commands
            System.out.println("\nPhoto Commands:");
            System.out.println("  list                - List all photos in the current album");
            System.out.println("  add_photo f# n# c# l# - Add a new photo to the album");
            System.out.println("                         f# - File path");
            System.out.println("                         n# - Photo name");
            System.out.println("                         c# - Caption");
            System.out.println("                         l# - Location");
            System.out.println("  select <index>      - Select a photo to view/edit");
            System.out.println("  delete <index>      - Delete a specific photo");
        }

        System.out.println("\nExample usage:");
        if (fsm == 0) {
            System.out.println("  add_trip n#Paris Trip d#Vacation in Paris");
        } else if (fsm == 1) {
            System.out.println("  add_photo f#path/to/image.jpg n#Eiffel Tower c#View from Trocadero");
        }

        System.out.println("======================");
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
